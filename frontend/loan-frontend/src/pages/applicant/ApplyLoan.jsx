import { useState,useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { CCard,CCardBody,CCardHeader,CRow,CCol,CForm,CFormInput,CFormSelect,CFormTextarea,CButton,CAlert,CSpinner } from '@coreui/react';
import api from '../../api/axiosConfig';

function ApplyLoan(){
 const [products,setProducts]=useState([]);
 const [formData,setFormData]=useState({productId:'',requestedAmount:'',tenureMonths:'',purpose:''});
 const [error,setError]=useState('');
 const [loading,setLoading]=useState(false);
 const navigate=useNavigate();
 const user=JSON.parse(localStorage.getItem('user'));

 useEffect(()=>{
  api.get('/products')
   .then(res=>setProducts(res.data))
   .catch(()=>setError('Could not load loan products.'));
 },[]);

 const handleChange=e=>setFormData({...formData,[e.target.name]:e.target.value});

 const handleSubmit=async e=>{
  e.preventDefault();
  setError('');
  setLoading(true);
  try{
   await api.post(`/applications/${user.userId}`,formData);
   navigate('/applicant/my-applications');
  }catch(err){
   setError(err.response?.data?.message||'Failed to submit application.');
  }finally{
   setLoading(false);
  }
 };

 return (
  <>
   <div className="mb-4">
    <h2 className="fw-bold">Apply for a Loan</h2>
    <p className="text-body-secondary">Submit a new loan application.</p>
   </div>

   <CCard>
    <CCardHeader className="fw-semibold">Loan Application</CCardHeader>
    <CCardBody>
     {error&&<CAlert color="danger">{error}</CAlert>}

     <CForm onSubmit={handleSubmit}>
      <CRow className="g-3">

       <CCol xs={12}>
        <CFormSelect
         label="Loan Product"
         name="productId"
         value={formData.productId}
         onChange={handleChange}
         required
         options={[
          {label:'-- Select Loan Product --',value:''},
          ...products.map(p=>({
           label:`Product #${p.productId} (₹${p.minAmount} - ₹${p.maxAmount}, ${p.defaultInterestRate}%)`,
           value:p.productId
          }))
         ]}
        />
       </CCol>

       <CCol md={6}>
        <CFormInput
         type="number"
         label="Requested Amount"
         name="requestedAmount"
         value={formData.requestedAmount}
         onChange={handleChange}
         min="1"
         required
        />
       </CCol>

       <CCol md={6}>
        <CFormInput
         type="number"
         label="Tenure (Months)"
         name="tenureMonths"
         value={formData.tenureMonths}
         onChange={handleChange}
         min="1"
         required
        />
       </CCol>

       <CCol xs={12}>
        <CFormTextarea
         label="Purpose"
         name="purpose"
         value={formData.purpose}
         onChange={handleChange}
         rows={4}
         placeholder="Enter the purpose of the loan"
         required
        />
       </CCol>

       <CCol xs={12}>
        <CButton type="submit" color="primary" disabled={loading}>
         {loading?<><CSpinner size="sm" className="me-2"/>Submitting...</>:'Submit Application'}
        </CButton>
       </CCol>

      </CRow>
     </CForm>
    </CCardBody>
   </CCard>
  </>
 );
}

export default ApplyLoan;