import { useEffect,useState } from 'react';
import { useNavigate,useParams } from 'react-router-dom';
import { CCard,CCardBody,CButton,CTable,CAlert,CForm,CFormInput } from '@coreui/react';
import api from '../../api/axiosConfig';

function ApplicationDetail(){
 const {applicationId}=useParams();
 const navigate=useNavigate();
 const [application,setApplication]=useState(null);
 const [documents,setDocuments]=useState([]);
 const [form,setForm]=useState({documentType:'',fileName:'',filePath:''});
 const [loading,setLoading]=useState(true);
 const [uploading,setUploading]=useState(false);
 const [error,setError]=useState('');
 const [message,setMessage]=useState('');

 const loadData=async()=>{
  try{
   setLoading(true);setError('');
   const app=await api.get(`/applications/${applicationId}`);
   setApplication(app.data);
   const docs=await api.get(`/documents/application/${applicationId}`);
   setDocuments(Array.isArray(docs.data)?docs.data:[]);
  }catch(e){
   setError(e.response?.data?.message||e.response?.data?.error||'Failed to load application.');
  }finally{setLoading(false);}
 };

 useEffect(()=>{loadData();},[applicationId]);

 const change=e=>setForm({...form,[e.target.name]:e.target.value});

 const uploadDocument=async e=>{
  e.preventDefault();setError('');setMessage('');
  if(!form.documentType||!form.fileName||!form.filePath){
   setError('Please fill all document fields.');return;
  }
  try{
   setUploading(true);
   await api.post(`/documents/application/${applicationId}`,form);
   setMessage('Document uploaded successfully.');
   setForm({documentType:'',fileName:'',filePath:''});
   await loadData();
  }catch(e){
   setError(e.response?.data?.message||e.response?.data?.error||'Document upload failed.');
  }finally{setUploading(false);}
 };

 if(loading)return <p>Loading application...</p>;
 if(!application)return <CAlert color="danger">{error||'Application not found.'}</CAlert>;

 return <>
  <div className="mb-4">
   <h2 className="fw-bold">Application #{application.applicationId}</h2>
   <p className="text-body-secondary">Loan application details</p>
  </div>

  {error&&<CAlert color="danger">{error}</CAlert>}
  {message&&<CAlert color="success">{message}</CAlert>}

  <CCard className="mb-4">
   <CCardBody>
    <CTable responsive className="mb-0">
     <tbody>
      <tr><th>Status</th><td><b>{application.status}</b></td></tr>
      <tr><th>Amount</th><td>₹{Number(application.requestedAmount||0).toLocaleString('en-IN')}</td></tr>
      <tr><th>Tenure</th><td>{application.tenureMonths} months</td></tr>
      <tr><th>Purpose</th><td>{application.purpose}</td></tr>
      <tr><th>Eligibility Score</th><td>{application.eligibilityScore??'-'}</td></tr>
     </tbody>
    </CTable>

    {application.status==='DISBURSED'&&
     <CButton color="success" className="mt-3"
      onClick={()=>navigate(`/applicant/application/${applicationId}/emi`)}>
      View EMI Schedule
     </CButton>
    }
   </CCardBody>
  </CCard>

  <CCard className="mb-4">
   <CCardBody>
    <h5 className="fw-semibold mb-3">Documents</h5>

    {documents.length===0?<p className="text-body-secondary">No documents uploaded yet.</p>:
     <CTable striped hover responsive>
      <thead><tr><th>ID</th><th>Type</th><th>File Name</th><th>Status</th><th>Remarks</th></tr></thead>
      <tbody>{documents.map(d=>
       <tr key={d.documentId}>
        <td>{d.documentId}</td>
        <td>{d.documentType}</td>
        <td>{d.fileName}</td>
        <td>{d.verificationStatus}</td>
        <td>{d.remarks||'-'}</td>
       </tr>
      )}</tbody>
     </CTable>
    }
   </CCardBody>
  </CCard>

  {application.status!=='DISBURSED'&&
   <CCard>
    <CCardBody>
     <h5 className="fw-semibold mb-3">Upload Document</h5>
     <CForm onSubmit={uploadDocument}>
      <CFormInput name="documentType" placeholder="Document type" value={form.documentType} onChange={change} className="mb-2"/>
      <CFormInput name="fileName" placeholder="File name" value={form.fileName} onChange={change} className="mb-2"/>
      <CFormInput name="filePath" placeholder="File path" value={form.filePath} onChange={change} className="mb-3"/>
      <CButton type="submit" disabled={uploading}>{uploading?'Uploading...':'Upload Document'}</CButton>
     </CForm>
    </CCardBody>
   </CCard>
  }
 </>;
}

export default ApplicationDetail;