import { useEffect,useState } from 'react';
import { useNavigate,useParams } from 'react-router-dom';
import { CCard,CCardBody,CButton,CTable,CAlert,CBadge,CSpinner } from '@coreui/react';
import api from '../../api/axiosConfig';

function EmiSchedule(){
 const {applicationId}=useParams();
 const navigate=useNavigate();
 const [emis,setEmis]=useState([]);
 const [error,setError]=useState('');
 const [loading,setLoading]=useState(true);

 useEffect(()=>{
  const load=async()=>{
   try{
    const d=await api.get(`/disbursements/application/${applicationId}`);
    const r=await api.get(`/emi/disbursement/${d.data.disbursementId}`);
    setEmis(Array.isArray(r.data)?r.data:[]);
   }catch(e){
    setError(e.response?.data?.message||e.response?.data?.error||'Failed to load EMI schedule.');
   }finally{
    setLoading(false);
   }
  };
  load();
 },[applicationId]);

 const statusColor={PAID:'success',OVERDUE:'danger',PENDING:'warning'};

 if(loading){
  return <div className="text-center py-5"><CSpinner/></div>;
 }

 return (
  <>
   <div className="mb-4">
    <h2 className="fw-bold">EMI Schedule</h2>
    <p className="text-body-secondary">Application #{applicationId}</p>
   </div>

   {error&&<CAlert color="danger">{error}</CAlert>}

   {!error&&(
    <CCard>
     <CCardBody>
      {emis.length===0&&<CAlert color="info">No EMI records found.</CAlert>}

      {emis.length>0&&(
       <CTable striped hover responsive align="middle">
        <thead>
         <tr>
          <th>EMI</th>
          <th>Due Date</th>
          <th>Amount</th>
          <th>Principal</th>
          <th>Interest</th>
          <th>Status</th>
          <th>Action</th>
         </tr>
        </thead>

        <tbody>
         {emis.map(e=>(
          <tr key={e.emiId}>
           <td>#{e.installmentNo}</td>
           <td>{e.dueDate?new Date(e.dueDate).toLocaleDateString():'-'}</td>
           <td>₹{Number(e.emiAmount||0).toLocaleString('en-IN',{minimumFractionDigits:2})}</td>
           <td>₹{Number(e.principalComponent||0).toLocaleString('en-IN',{minimumFractionDigits:2})}</td>
           <td>₹{Number(e.interestComponent||0).toLocaleString('en-IN',{minimumFractionDigits:2})}</td>
           <td>
            <CBadge color={statusColor[e.status]||'secondary'}>
             {e.status}
            </CBadge>
           </td>
           <td>
            {e.status!=='PAID'&&(
             <CButton
              size="sm"
              color="primary"
              onClick={()=>navigate(`/applicant/pay/${e.emiId}`)}
             >
              Pay EMI
             </CButton>
            )}
           </td>
          </tr>
         ))}
        </tbody>
       </CTable>
      )}
     </CCardBody>
    </CCard>
   )}

  </>
 );
}

export default EmiSchedule;