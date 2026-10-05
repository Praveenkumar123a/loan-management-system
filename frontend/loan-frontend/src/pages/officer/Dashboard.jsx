import { useEffect,useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { CCard,CCardBody,CTable,CButton,CAlert,CBadge,CModal,CModalHeader,CModalTitle,CModalBody,CModalFooter,CFormInput,CFormTextarea,CSpinner } from '@coreui/react';
import api from '../../api/axiosConfig';

function Dashboard(){
 const navigate=useNavigate();
 const user=JSON.parse(localStorage.getItem('user'));
 const [applications,setApplications]=useState([]);
 const [selected,setSelected]=useState(null);
 const [documents,setDocuments]=useState([]);
 const [error,setError]=useState('');
 const [message,setMessage]=useState('');
 const [loading,setLoading]=useState(true);
 const [remarks,setRemarks]=useState('');
 const [comments,setComments]=useState('');
 const [processing,setProcessing]=useState(false);

 const loadApplications=async()=>{
  try{
   setLoading(true);setError('');
   const r=await api.get('/applications/status/SUBMITTED');
   setApplications(Array.isArray(r.data)?r.data:[]);
  }catch(e){
   setError(e.response?.data?.message||e.response?.data?.error||'Failed to load applications.');
  }finally{setLoading(false);}
 };

 useEffect(()=>{loadApplications();},[]);

 const reviewApplication=async app=>{
  try{
   setError('');setMessage('');setSelected(app);setRemarks('');setComments('');
   const r=await api.get(`/documents/application/${app.applicationId}`);
   setDocuments(Array.isArray(r.data)?r.data:[]);
  }catch(e){
   setError(e.response?.data?.message||e.response?.data?.error||'Failed to load documents.');
  }
 };

 const verifyDocument=async(documentId,verified)=>{
  try{
   setProcessing(true);setError('');setMessage('');
   await api.put(`/documents/${documentId}/verify`,{verified,remarks});
   setMessage(verified?'Document verified successfully.':'Document marked for resubmission.');
   setRemarks('');
   const r=await api.get(`/documents/application/${selected.applicationId}`);
   setDocuments(Array.isArray(r.data)?r.data:[]);
  }catch(e){
   setError(e.response?.data?.message||e.response?.data?.error||'Document verification failed.');
  }finally{setProcessing(false);}
 };

 const recommend=async decision=>{
  if(!selected)return;
  try{
   setProcessing(true);setError('');setMessage('');
   await api.post(`/recommendations/application/${selected.applicationId}`,{
    officerId:user.userId,recommend:decision,comments
   });
   setMessage(decision?'Application recommended successfully.':'Application marked as not recommended.');
   setComments('');setSelected(null);setDocuments([]);
   await loadApplications();
  }catch(e){
   setError(e.response?.data?.message||e.response?.data?.error||'Recommendation failed.');
  }finally{setProcessing(false);}
 };

 return <>
  <div className="mb-4">
   <h2 className="fw-bold">Loan Officer Dashboard</h2>
   <p className="text-body-secondary">
    Welcome, <b>{user?.firstName} {user?.lastName}</b>
   </p>
  </div>

  {error&&<CAlert color="danger">{error}</CAlert>}
  {message&&<CAlert color="success">{message}</CAlert>}

  <CCard>
   <CCardBody>
    <div className="d-flex justify-content-between align-items-center mb-3">
     <h5 className="fw-semibold mb-0">Submitted Applications</h5>
     <CBadge color="info">{applications.length} Applications</CBadge>
    </div>

    {loading?<div className="text-center py-4"><CSpinner/> </div>:
     applications.length===0?<CAlert color="info">No submitted applications found.</CAlert>:
     <CTable striped hover responsive align="middle">
      <thead>
       <tr>
        <th>ID</th><th>Applicant</th><th>Amount</th><th>Tenure</th>
        <th>Eligibility</th><th>Status</th><th>Action</th>
       </tr>
      </thead>
      <tbody>
       {applications.map(app=>(
        <tr key={app.applicationId}>
         <td>#{app.applicationId}</td>
         <td>{app.applicantId}</td>
         <td>₹{Number(app.requestedAmount||0).toLocaleString('en-IN')}</td>
         <td>{app.tenureMonths} months</td>
         <td>{app.eligibilityScore??'-'}</td>
         <td><CBadge color="info">{app.status}</CBadge></td>
         <td>
          <CButton size="sm" color="primary" onClick={()=>reviewApplication(app)}>
           Review
          </CButton>
         </td>
        </tr>
       ))}
      </tbody>
     </CTable>
    }
   </CCardBody>
  </CCard>

  <CModal size="xl" visible={!!selected} onClose={()=>{setSelected(null);setDocuments([]);}}>
   <CModalHeader>
    <CModalTitle>Application #{selected?.applicationId}</CModalTitle>
   </CModalHeader>

   <CModalBody>
    {selected&&<>
     <div className="row g-3 mb-4">
      <div className="col-md-4"><b>Applicant:</b> {selected.applicantId}</div>
      <div className="col-md-4"><b>Product:</b> {selected.productId}</div>
      <div className="col-md-4"><b>Amount:</b> ₹{Number(selected.requestedAmount||0).toLocaleString('en-IN')}</div>
      <div className="col-md-4"><b>Tenure:</b> {selected.tenureMonths} months</div>
      <div className="col-md-4"><b>Eligibility:</b> {selected.eligibilityScore??'-'}</div>
      <div className="col-md-4"><b>Status:</b> {selected.status}</div>
      <div className="col-12"><b>Purpose:</b> {selected.purpose}</div>
     </div>

     <h6 className="fw-semibold">Documents</h6>

     {documents.length===0?<CAlert color="info">No documents uploaded.</CAlert>:
      <CTable striped hover responsive>
       <thead>
        <tr><th>ID</th><th>Type</th><th>File</th><th>Status</th><th>Remarks</th><th>Action</th></tr>
       </thead>
       <tbody>
        {documents.map(doc=>(
         <tr key={doc.documentId}>
          <td>{doc.documentId}</td>
          <td>{doc.documentType}</td>
          <td>{doc.fileName}</td>
          <td><CBadge color={doc.verificationStatus==='VERIFIED'?'success':doc.verificationStatus==='RESUBMISSION_REQUIRED'?'danger':'warning'}>{doc.verificationStatus}</CBadge></td>
          <td>{doc.remarks||'-'}</td>
          <td>
           {doc.verificationStatus==='PENDING'&&
            <div className="d-flex gap-1">
             <CButton size="sm" color="success" disabled={processing} onClick={()=>verifyDocument(doc.documentId,true)}>Verify</CButton>
             <CButton size="sm" color="danger" disabled={processing} onClick={()=>verifyDocument(doc.documentId,false)}>Resubmit</CButton>
            </div>
           }
          </td>
         </tr>
        ))}
       </tbody>
      </CTable>
     }

     <CFormInput
      label="Document Remarks"
      value={remarks}
      onChange={e=>setRemarks(e.target.value)}
      placeholder="Enter remarks before verification"
      className="mb-3"
     />

     <CFormTextarea
      label="Loan Recommendation Comments"
      value={comments}
      onChange={e=>setComments(e.target.value)}
      placeholder="Enter recommendation comments"
      rows={3}
     />
    </>}
   </CModalBody>

   <CModalFooter>
    <CButton color="success" disabled={processing} onClick={()=>recommend(true)}>
     Recommend
    </CButton>
    <CButton color="danger" disabled={processing} onClick={()=>recommend(false)}>
     Not Recommend
    </CButton>
    <CButton color="secondary" onClick={()=>{setSelected(null);setDocuments([]);}}>
     Close
    </CButton>
   </CModalFooter>
  </CModal>
 </>;
}

export default Dashboard;