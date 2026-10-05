import { useNavigate } from 'react-router-dom';
import { CCard,CCardBody,CCol,CRow,CButton,CBadge } from '@coreui/react';

function ApplicantDashboard(){
  const navigate=useNavigate();
  const user=JSON.parse(localStorage.getItem('user'));

  return (
    <>
      <div className="mb-4">
        <h2 className="fw-bold mb-1">Applicant Dashboard</h2>
        <p className="text-body-secondary mb-0">
          Welcome, <strong>{user?.firstName} {user?.lastName}</strong>
        </p>
      </div>

      <CRow className="g-4 mb-4">

        <CCol sm={6} lg={4}>
          <CCard className="h-100 shadow-sm">
            <CCardBody>
              <div className="d-flex justify-content-between align-items-start mb-3">
                <div>
                  <div className="text-body-secondary text-uppercase small fw-semibold">
                    Profile
                  </div>
                  <h4 className="mt-2 mb-0">KYC</h4>
                </div>
                <CBadge color="warning">Required</CBadge>
              </div>

              <p className="text-body-secondary">
                Complete your applicant profile and KYC details before applying for a loan.
              </p>

              <CButton color="warning" onClick={()=>navigate('/applicant/kyc')}>
                Complete KYC
              </CButton>
            </CCardBody>
          </CCard>
        </CCol>

        <CCol sm={6} lg={4}>
          <CCard className="h-100 shadow-sm">
            <CCardBody>
              <div className="d-flex justify-content-between align-items-start mb-3">
                <div>
                  <div className="text-body-secondary text-uppercase small fw-semibold">
                    Loan
                  </div>
                  <h4 className="mt-2 mb-0">Apply for Loan</h4>
                </div>
                <CBadge color="primary">New</CBadge>
              </div>

              <p className="text-body-secondary">
                Choose a loan product and submit a new loan application.
              </p>

              <CButton color="primary" onClick={()=>navigate('/applicant/apply')}>
                Apply for Loan
              </CButton>
            </CCardBody>
          </CCard>
        </CCol>

        <CCol sm={6} lg={4}>
          <CCard className="h-100 shadow-sm">
            <CCardBody>
              <div className="d-flex justify-content-between align-items-start mb-3">
                <div>
                  <div className="text-body-secondary text-uppercase small fw-semibold">
                    Applications
                  </div>
                  <h4 className="mt-2 mb-0">My Applications</h4>
                </div>
                <CBadge color="success">View</CBadge>
              </div>

              <p className="text-body-secondary">
                Track your submitted applications, loan status and EMI schedule.
              </p>

              <CButton color="success" onClick={()=>navigate('/applicant/my-applications')}>
                My Applications
              </CButton>
            </CCardBody>
          </CCard>
        </CCol>

      </CRow>

      <CRow className="g-4">

        <CCol md={6}>
          <CCard className="shadow-sm">
            <CCardBody>
              <h5 className="fw-semibold">Loan Process</h5>
              <p className="text-body-secondary">
                Follow the complete loan process from application to repayment.
              </p>

              <div className="d-flex flex-wrap gap-2">
                <CBadge color="secondary">Application</CBadge>
                <CBadge color="info">Verification</CBadge>
                <CBadge color="primary">Recommendation</CBadge>
                <CBadge color="warning">Approval</CBadge>
                <CBadge color="success">Disbursement</CBadge>
                <CBadge color="dark">EMI Payment</CBadge>
              </div>
            </CCardBody>
          </CCard>
        </CCol>

        <CCol md={6}>
          <CCard className="shadow-sm">
            <CCardBody>
              <h5 className="fw-semibold">Quick Actions</h5>
              <div className="d-flex flex-wrap gap-2 mt-3">
                <CButton
                  color="primary"
                  variant="outline"
                  onClick={()=>navigate('/applicant/apply')}
                >
                  Apply Loan
                </CButton>

                <CButton
                  color="success"
                  variant="outline"
                  onClick={()=>navigate('/applicant/my-applications')}
                >
                  View Applications
                </CButton>

                <CButton
                  color="warning"
                  variant="outline"
                  onClick={()=>navigate('/applicant/kyc')}
                >
                  Update KYC
                </CButton>
              </div>
            </CCardBody>
          </CCard>
        </CCol>

      </CRow>
    </>
  );
}

export default ApplicantDashboard;