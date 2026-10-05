import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { CCard,CCardBody,CCardHeader,CRow,CCol,CForm,CFormInput,CFormTextarea,CFormSelect,CButton,CAlert,CSpinner } from '@coreui/react';
import api from '../../api/axiosConfig';

function Kyc(){
 const navigate=useNavigate();
 const user=JSON.parse(localStorage.getItem('user'));
 const [form,setForm]=useState({dateOfBirth:'',address:'',panNumber:'',aadhaarNumber:'',employmentType:'SALARIED',monthlyIncome:'',existingLiabilities:'0'});
 const [error,setError]=useState('');
 const [msg,setMsg]=useState('');
 const [loading,setLoading]=useState(false);

 const change=e=>setForm({...form,[e.target.name]:e.target.value});

 const submit=async e=>{
  e.preventDefault();setError('');setMsg('');setLoading(true);
  try{
   await api.post(`/applicant/profile/${user.userId}`,{
    dateOfBirth:form.dateOfBirth,
    address:form.address,
    panNumber:form.panNumber,
    aadhaarNumber:form.aadhaarNumber,
    employmentType:form.employmentType,
    monthlyIncome:Number(form.monthlyIncome),
    existingLiabilities:Number(form.existingLiabilities||0)
   });
   setMsg('KYC completed successfully.');
   setTimeout(()=>navigate('/applicant/apply'),1000);
  }catch(e){
   setError(e.response?.data?.message||e.response?.data?.error||'KYC submission failed.');
  }finally{setLoading(false);}
 };

 return (
  <>
   <div className="mb-4">
    <h2 className="fw-bold">Applicant KYC</h2>
    <p className="text-body-secondary">Complete your profile before applying for a loan.</p>
   </div>

   <CCard>
    <CCardHeader className="fw-semibold">KYC Details</CCardHeader>
    <CCardBody>
     {error&&<CAlert color="danger">{error}</CAlert>}
     {msg&&<CAlert color="success">{msg}</CAlert>}

     <CForm onSubmit={submit}>
      <CRow className="g-3">
       <CCol md={6}>
        <CFormInput
         type="date"
         label="Date of Birth"
         name="dateOfBirth"
         value={form.dateOfBirth}
         onChange={change}
         required
        />
       </CCol>

       <CCol md={6}>
        <CFormInput
         label="PAN Number"
         name="panNumber"
         value={form.panNumber}
         onChange={change}
         maxLength={10}
         required
        />
       </CCol>

       <CCol md={6}>
        <CFormInput
         label="Aadhaar Number"
         name="aadhaarNumber"
         value={form.aadhaarNumber}
         onChange={change}
         maxLength={12}
         required
        />
       </CCol>

       <CCol md={6}>
        <CFormSelect
         label="Employment Type"
         name="employmentType"
         value={form.employmentType}
         onChange={change}
         options={[
          {label:'Salaried',value:'SALARIED'},
          {label:'Self Employed',value:'SELF_EMPLOYED'},
          {label:'Business',value:'BUSINESS'},
          {label:'Student',value:'STUDENT'}
         ]}
        />
       </CCol>

       <CCol xs={12}>
        <CFormTextarea
         label="Address"
         name="address"
         value={form.address}
         onChange={change}
         rows={3}
         required
        />
       </CCol>

       <CCol md={6}>
        <CFormInput
         type="number"
         label="Monthly Income"
         name="monthlyIncome"
         value={form.monthlyIncome}
         onChange={change}
         min="1"
         required
        />
       </CCol>

       <CCol md={6}>
        <CFormInput
         type="number"
         label="Existing Liabilities"
         name="existingLiabilities"
         value={form.existingLiabilities}
         onChange={change}
         min="0"
        />
       </CCol>

       <CCol xs={12}>
        <CButton type="submit" color="primary" disabled={loading}>
         {loading?<><CSpinner size="sm" className="me-2"/>Saving...</>:'Complete KYC'}
        </CButton>
       </CCol>
      </CRow>
     </CForm>
    </CCardBody>
   </CCard>
  </>
 );
}

export default Kyc;