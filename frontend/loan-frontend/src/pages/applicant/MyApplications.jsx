import { useEffect,useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { CCard,CCardBody,CTable,CButton,CBadge,CAlert } from '@coreui/react';
import api from '../../api/axiosConfig';

function MyApplications(){
 const [applications,setApplications]=useState([]);
 const [error,setError]=useState('');
 const navigate=useNavigate();
 const user=JSON.parse(localStorage.getItem('user'));

 useEffect(()=>{
  if(!user?.userId){navigate('/login');return;}
  api.get(`/applications/applicant/${user.userId}`)
   .then(r=>setApplications(r.data))
   .catch(()=>setError('Could not load your applications.'));
 },[navigate,user?.userId]);

 const statusColor={
  SUBMITTED:'info',
  VERIFIED:'primary',
  RECOMMENDED:'warning',
  APPROVED:'success',
  REJECTED:'danger',
  DISBURSED:'success',
  CLOSED:'dark'
 };

 return (
  <>
   <div className="d-flex justify-content-between align-items-center mb-4">
    <div>
     <h2 className="fw-bold mb-1">My Applications</h2>
     <p className="text-body-secondary mb-0">Track your loan applications and status.</p>
    </div>
    <CButton color="primary" onClick={()=>navigate('/applicant/apply')}>Apply for Loan</CButton>
   </div>

   {error&&<CAlert color="danger">{error}</CAlert>}

   <CCard>
    <CCardBody>
     {applications.length===0 ? (
      <div className="text-center py-5">
       <h5>No loan applications found</h5>
       <CButton color="primary" className="mt-2" onClick={()=>navigate('/applicant/apply')}>
        Apply for a Loan
       </CButton>
      </div>
     ) : (
      <CTable striped hover responsive align="middle">
       <thead>
        <tr>
         <th>ID</th>
         <th>Product</th>
         <th>Amount</th>
         <th>Tenure</th>
         <th>Eligibility</th>
         <th>Status</th>
         <th>Submitted</th>
         <th>Action</th>
        </tr>
       </thead>
       <tbody>
        {applications.map(a=>(
         <tr key={a.applicationId}>
          <td>#{a.applicationId}</td>
          <td>{a.productId}</td>
          <td>₹{Number(a.requestedAmount||0).toLocaleString('en-IN')}</td>
          <td>{a.tenureMonths} months</td>
          <td>{a.eligibilityScore??'-'}</td>
          <td><CBadge color={statusColor[a.status]||'secondary'}>{a.status}</CBadge></td>
          <td>{a.submittedAt?new Date(a.submittedAt).toLocaleDateString():'-'}</td>
          <td>
           <CButton size="sm" color="primary" variant="outline" onClick={()=>navigate(`/applicant/application/${a.applicationId}`)}>
            View
           </CButton>
          </td>
         </tr>
        ))}
       </tbody>
      </CTable>
     )}
    </CCardBody>
   </CCard>
  </>
 );
}

export default MyApplications;