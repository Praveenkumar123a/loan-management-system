import { useState } from 'react';
import { useNavigate,useParams } from 'react-router-dom';
import { CCard,CCardBody,CCardHeader,CForm,CFormSelect,CFormInput,CButton,CAlert,CSpinner } from '@coreui/react';
import api from '../../api/axiosConfig';

function PayEmi(){
 const {emiId}=useParams();
 const navigate=useNavigate();
 const [form,setForm]=useState({paymentMode:'UPI',transactionRef:''});
 const [error,setError]=useState('');
 const [msg,setMsg]=useState('');
 const [loading,setLoading]=useState(false);

 const change=e=>setForm({...form,[e.target.name]:e.target.value});

 const pay=async e=>{
  e.preventDefault();
  setError('');setMsg('');setLoading(true);
  try{
   await api.post(`/payments/emi/${emiId}`,form);
   setMsg('EMI payment completed successfully.');
   setTimeout(()=>navigate(-1),1200);
  }catch(e){
   setError(e.response?.data?.message||'Payment failed. Please try again.');
  }finally{
   setLoading(false);
  }
 };

 return (
  <>
   <div className="mb-4">
    <h2 className="fw-bold">Pay EMI</h2>
    <p className="text-body-secondary">Complete your EMI payment</p>
   </div>

   <CCard className="mx-auto" style={{maxWidth:500}}>
    <CCardHeader className="fw-semibold">Payment Details</CCardHeader>

    <CCardBody>
     <div className="mb-3">
      <span className="text-body-secondary">EMI ID</span>
      <div className="fw-bold">#{emiId}</div>
     </div>

     {error&&<CAlert color="danger">{error}</CAlert>}
     {msg&&<CAlert color="success">{msg}</CAlert>}

     <CForm onSubmit={pay}>
      <CFormSelect
       label="Payment Mode"
       name="paymentMode"
       value={form.paymentMode}
       onChange={change}
       className="mb-3"
       options={[
        {label:'UPI',value:'UPI'},
        {label:'Bank Transfer',value:'BANK_TRANSFER'},
        {label:'Card',value:'CARD'},
        {label:'Cash',value:'CASH'}
       ]}
      />

      <CFormInput
       label="Transaction Reference"
       name="transactionRef"
       value={form.transactionRef}
       onChange={change}
       placeholder="Enter transaction reference"
       required
       className="mb-4"
      />

      <CButton type="submit" color="success" className="w-100" disabled={loading}>
       {loading?<><CSpinner size="sm" className="me-2"/>Processing...</>:'Pay EMI'}
      </CButton>
     </CForm>
    </CCardBody>
   </CCard>
  </>
 );
}

export default PayEmi;