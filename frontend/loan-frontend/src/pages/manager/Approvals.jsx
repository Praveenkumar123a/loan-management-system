import { useEffect,useState } from 'react';
import { CCard,CCardBody,CTable,CButton,CBadge,CAlert,CModal,CModalHeader,CModalTitle,CModalBody,CModalFooter,CFormSelect,CFormInput,CFormTextarea } from '@coreui/react';
import api from '../../api/axiosConfig';

function Approvals(){
 const user=JSON.parse(localStorage.getItem('user'));
 const [apps,setApps]=useState([]),[app,setApp]=useState(null),[approval,setApproval]=useState(null);
 const [documents,setDocuments]=useState([]);
 const [error,setError]=useState(''),[msg,setMsg]=useState('');
 const [form,setForm]=useState({approve:true,comments:'',approvedAmount:'',interestRate:'',tenureMonths:''});

 const load=async()=>{
  try{
   setError('');
   const r=await api.get('/applications/status/RECOMMENDED');
   setApps(Array.isArray(r.data)?r.data:[]);
  }catch(e){
   setError(e.response?.data?.message||'Failed to load recommended applications.');
  }
 };

 useEffect(()=>{load();},[]);

 const open=async a=>{
  setApp(a);
  setError('');
  setMsg('');
  setApproval(null);
  setDocuments([]);
  setForm({
   approve:true,
   comments:'',
   approvedAmount:a.requestedAmount||'',
   interestRate:'',
   tenureMonths:a.tenureMonths||''
  });

  try{
   const docs=await api.get(`/documents/application/${a.applicationId}`);
   setDocuments(Array.isArray(docs.data)?docs.data:[]);
  }catch(e){
   setError(e.response?.data?.message||'Failed to load documents.');
  }

  try{
   const r=await api.get(`/approvals/application/${a.applicationId}`);
   if(r.data)setApproval(r.data);
  }catch{
   setApproval(null);
  }
 };

 const change=e=>setForm({
  ...form,
  [e.target.name]:e.target.name==='approve'?e.target.value==='true':e.target.value
 });

 const decide=async e=>{
  e.preventDefault();

  try{
   setError('');
   setMsg('');

   await api.post(`/approvals/application/${app.applicationId}`,{
    managerId:user.userId,
    approve:form.approve,
    comments:form.comments,
    approvedAmount:Number(form.approvedAmount),
    interestRate:Number(form.interestRate),
    tenureMonths:Number(form.tenureMonths)
   });

   setMsg(form.approve?'Loan approved successfully.':'Loan rejected successfully.');

   const r=await api.get(`/approvals/application/${app.applicationId}`);
   setApproval(r.data);
   await load();
  }catch(e){
   setError(e.response?.data?.message||'Approval failed.');
  }
 };

 const disburse=async()=>{
  try{
   setError('');
   setMsg('');

   await api.post(`/disbursements/application/${app.applicationId}`,{
    disbursedById:user.userId,
    disbursedDate:new Date().toISOString().split('T')[0]
   });

   setMsg('Loan disbursed successfully.');
   await load();
  }catch(e){
   setError(e.response?.data?.message||'Disbursement failed.');
  }
 };

 const close=()=>{
  setApp(null);
  setApproval(null);
  setDocuments([]);
  setError('');
  setMsg('');
 };

 return <>
  <div className="mb-4">
   <h2 className="fw-bold">Loan Approvals</h2>
   <p className="text-body-secondary">Welcome, {user?.firstName} {user?.lastName}</p>
  </div>

  {error&&<CAlert color="danger">{error}</CAlert>}
  {msg&&<CAlert color="success">{msg}</CAlert>}

  <CCard>
   <CCardBody>
    <div className="d-flex justify-content-between align-items-center mb-3">
     <h5 className="fw-semibold mb-0">Recommended Applications</h5>
     <CBadge color="info">{apps.length} Applications</CBadge>
    </div>

    {apps.length===0?
     <CAlert color="info">No recommended applications.</CAlert>:
     <CTable striped hover responsive align="middle">
      <thead>
       <tr>
        <th>ID</th>
        <th>Applicant</th>
        <th>Amount</th>
        <th>Score</th>
        <th>Status</th>
        <th>Action</th>
       </tr>
      </thead>

      <tbody>
       {apps.map(a=>
        <tr key={a.applicationId}>
         <td>#{a.applicationId}</td>
         <td>{a.applicantName||a.applicantId}</td>
         <td>₹{Number(a.requestedAmount||0).toLocaleString('en-IN')}</td>
         <td><CBadge color="info">{a.eligibilityScore??'-'}</CBadge></td>
         <td><CBadge color="success">{a.status}</CBadge></td>
         <td>
          <CButton size="sm" color="primary" onClick={()=>open(a)}>
           Review
          </CButton>
         </td>
        </tr>
       )}
      </tbody>
     </CTable>
    }
   </CCardBody>
  </CCard>

  <CModal size="xl" visible={!!app} onClose={close}>
   <CModalHeader>
    <CModalTitle>Application #{app?.applicationId}</CModalTitle>
   </CModalHeader>

   <CModalBody>
    {error&&<CAlert color="danger">{error}</CAlert>}
    {msg&&<CAlert color="success">{msg}</CAlert>}

    {app&&
     <div className="row g-3 mb-4">
      <div className="col-md-6"><b>Applicant:</b> {app.applicantName||app.applicantId}</div>
      <div className="col-md-6"><b>Amount:</b> ₹{Number(app.requestedAmount||0).toLocaleString('en-IN')}</div>
      <div className="col-md-6"><b>Tenure:</b> {app.tenureMonths} months</div>
      <div className="col-md-6"><b>Eligibility:</b> {app.eligibilityScore??'-'}</div>
      <div className="col-md-6"><b>Status:</b> {app.status}</div>
     </div>
    }

    <h6 className="fw-semibold mb-3">Documents</h6>

    {documents.length===0?
     <CAlert color="info">No documents uploaded.</CAlert>:
     <CTable striped hover responsive className="mb-4">
      <thead>
       <tr>
        <th>ID</th>
        <th>Type</th>
        <th>File Name</th>
        <th>Status</th>
        <th>Remarks</th>
        <th>Action</th>
       </tr>
      </thead>

      <tbody>
       {documents.map(doc=>
        <tr key={doc.documentId}>
         <td>{doc.documentId}</td>
         <td>{doc.documentType}</td>
         <td>{doc.fileName}</td>

         <td>
          <CBadge
           color={
            doc.verificationStatus==='VERIFIED'
             ?'success'
             :doc.verificationStatus==='RESUBMISSION_REQUIRED'
             ?'danger'
             :'warning'
           }
          >
           {doc.verificationStatus}
          </CBadge>
         </td>

         <td>{doc.remarks||'-'}</td>

         <td>
          <CButton
           size="sm"
           color="primary"
           variant="outline"
           onClick={()=>window.open(`${api.defaults.baseURL}/documents/${doc.documentId}/file`,'_blank')}
          >
           View
          </CButton>
         </td>
        </tr>
       )}
      </tbody>
     </CTable>
    }

    {approval?
     <CAlert color={approval.decision==='APPROVED'?'success':'danger'}>
      <h6 className="fw-bold">{approval.decision}</h6>
      <div>Amount: ₹{Number(approval.approvedAmount||0).toLocaleString('en-IN')}</div>
      <div>Interest: {approval.interestRate}%</div>
      <div>Tenure: {approval.tenureMonths} months</div>

      {approval.decision==='APPROVED'&&
       <CButton color="success" className="mt-3" onClick={disburse}>
        Disburse Loan
       </CButton>
      }
     </CAlert>
    :
     <form onSubmit={decide}>
      <CFormSelect
       label="Decision"
       name="approve"
       value={String(form.approve)}
       onChange={change}
       className="mb-3"
       options={[
        {label:'Approve',value:'true'},
        {label:'Reject',value:'false'}
       ]}
      />

      <CFormInput
       type="number"
       label="Approved Amount"
       name="approvedAmount"
       value={form.approvedAmount}
       onChange={change}
       required
       className="mb-3"
      />

      <CFormInput
       type="number"
       step="0.01"
       label="Interest Rate"
       name="interestRate"
       value={form.interestRate}
       onChange={change}
       required
       className="mb-3"
      />

      <CFormInput
       type="number"
       label="Tenure Months"
       name="tenureMonths"
       value={form.tenureMonths}
       onChange={change}
       required
       className="mb-3"
      />

      <CFormTextarea
       label="Comments"
       name="comments"
       value={form.comments}
       onChange={change}
       rows={3}
       required
       className="mb-3"
      />

      <CButton type="submit" color={form.approve?'success':'danger'}>
       {form.approve?'Approve':'Reject'}
      </CButton>
     </form>
    }
   </CModalBody>

   <CModalFooter>
    <CButton color="secondary" onClick={close}>
     Close
    </CButton>
   </CModalFooter>
  </CModal>
 </>;
}

export default Approvals;