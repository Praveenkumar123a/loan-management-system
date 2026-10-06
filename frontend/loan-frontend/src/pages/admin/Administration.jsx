import { CCard,CCardBody,CRow,CCol,CButton,CBadge } from '@coreui/react';

function Administration(){
  return (
    <>
      <div className="mb-4">
        <h2 className="fw-bold">Administration</h2>
        <p className="text-body-secondary">
          Manage loan products and eligibility rules.
        </p>
      </div>

      <CRow className="g-4">

        <CCol md={6}>
          <CCard className="h-100 shadow-sm">
            <CCardBody>
              <CBadge color="primary" className="mb-3">
                ADMIN
              </CBadge>

              <h5 className="fw-semibold">
                Loan Products
              </h5>

              <p className="text-body-secondary">
                Manage loan products, amounts, interest rates and tenure.
              </p>

              <CButton color="primary" disabled>
                Manage Products
              </CButton>
            </CCardBody>
          </CCard>
        </CCol>

        <CCol md={6}>
          <CCard className="h-100 shadow-sm">
            <CCardBody>
              <CBadge color="warning" className="mb-3">
                SETTINGS
              </CBadge>

              <h5 className="fw-semibold">
                Eligibility Rules
              </h5>

              <p className="text-body-secondary">
                Manage eligibility rules used during loan processing.
              </p>

              <CButton color="warning" disabled>
                Manage Rules
              </CButton>
            </CCardBody>
          </CCard>
        </CCol>

      </CRow>
    </>
  );
}

export default Administration;