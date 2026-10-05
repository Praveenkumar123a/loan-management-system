import { useState } from 'react';
import { NavLink,useNavigate } from 'react-router-dom';
import {
  CSidebar,
  CSidebarBrand,
  CSidebarHeader,
  CSidebarNav,
  CSidebarToggler,
  CHeader,
  CHeaderBrand,
  CHeaderNav,
  CNavItem,
  CNavLink,
  CContainer,
  CButton
} from '@coreui/react';

function AppLayout({children}){
  const navigate=useNavigate();
  const [sidebarShow,setSidebarShow]=useState(true);
  const user=JSON.parse(localStorage.getItem('user'));

  const logout=()=>{
    localStorage.removeItem('user');
    navigate('/login');
  };

  const role=user?.role;

  return (
    <div className="d-flex min-vh-100">
      <CSidebar
        position="fixed"
        visible={sidebarShow}
        className="border-end"
      >
        <CSidebarHeader>
          <CSidebarBrand className="fw-bold">
            Loan LMS
          </CSidebarBrand>
        </CSidebarHeader>

        <CSidebarNav>
          <CNavItem>
            <CNavLink component={NavLink} to={
              role==='APPLICANT'?'/applicant/dashboard':
              role==='LOAN_OFFICER'?'/officer/dashboard':
              role==='MANAGER'?'/manager/dashboard':
              '/admin/dashboard'
            }>
              🏠 Dashboard
            </CNavLink>
          </CNavItem>

          {role==='APPLICANT' && (
            <>
              <CNavItem>
                <CNavLink component={NavLink} to="/applicant/kyc">
                  👤 KYC
                </CNavLink>
              </CNavItem>

              <CNavItem>
                <CNavLink component={NavLink} to="/applicant/apply">
                  💰 Apply for Loan
                </CNavLink>
              </CNavItem>

              <CNavItem>
                <CNavLink component={NavLink} to="/applicant/my-applications">
                  📄 My Applications
                </CNavLink>
              </CNavItem>
            </>
          )}

          {role==='LOAN_OFFICER' && (
            <CNavItem>
              <CNavLink component={NavLink} to="/officer/dashboard">
                📋 Loan Applications
              </CNavLink>
            </CNavItem>
          )}

          {role==='MANAGER' && (
            <CNavItem>
              <CNavLink component={NavLink} to="/manager/dashboard">
                ✅ Loan Approvals
              </CNavLink>
            </CNavItem>
          )}

          {role==='ADMIN' && (
            <CNavItem>
              <CNavLink component={NavLink} to="/admin/dashboard">
                ⚙️ Administration
              </CNavLink>
            </CNavItem>
          )}
        </CSidebarNav>

        <div className="mt-auto p-3 border-top">
          <div className="small text-body-secondary mb-2">
            Logged in as
          </div>
          <div className="fw-semibold">
            {user?.firstName} {user?.lastName}
          </div>
          <div className="small text-body-secondary mb-3">
            {role}
          </div>
          <CButton color="danger" variant="outline" size="sm" className="w-100" onClick={logout}>
            Logout
          </CButton>
        </div>

        <CSidebarToggler
          className="d-none d-lg-flex"
          onClick={()=>setSidebarShow(!sidebarShow)}
        />
      </CSidebar>

      <div className="wrapper flex-grow-1" style={{marginLeft:sidebarShow?'256px':'0'}}>
        <CHeader className="border-bottom px-4">
          <CContainer fluid>
            <CHeaderBrand className="fw-semibold">
              Loan Management System
            </CHeaderBrand>

            <CHeaderNav className="ms-auto">
              <CNavItem>
                <CNavLink>
                  {user?.firstName} {user?.lastName}
                </CNavLink>
              </CNavItem>
            </CHeaderNav>
          </CContainer>
        </CHeader>

        <main className="p-4">
          {children}
        </main>
      </div>
    </div>
  );
}

export default AppLayout;