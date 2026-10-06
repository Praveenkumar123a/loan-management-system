import { useState } from 'react';
import { useNavigate,useLocation } from 'react-router-dom';
import {
  CSidebar,
  CSidebarBrand,
  CSidebarHeader,
  CSidebarNav,
  CSidebarToggler,
  CNavItem,
  CNavLink,
  CHeader,
  CHeaderBrand,
  CHeaderNav,
  CContainer,
  CButton
} from '@coreui/react';

function AppLayout({children}){
  const navigate=useNavigate();
  const location=useLocation();
  const [sidebarShow,setSidebarShow]=useState(true);

  const user=JSON.parse(localStorage.getItem('user'));
  const role=user?.role;

  const logout=()=>{
    localStorage.removeItem('user');
    navigate('/login');
  };

  const dashboardPath=
    role==='APPLICANT'?'/applicant/dashboard':
    role==='LOAN_OFFICER'?'/officer/dashboard':
    role==='MANAGER'?'/manager/dashboard':
    '/admin/dashboard';

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
            <CNavLink
              active={location.pathname===dashboardPath}
              onClick={()=>navigate(dashboardPath)}
              style={{cursor:'pointer'}}
            >
              🏠 Dashboard
            </CNavLink>
          </CNavItem>

          {role==='APPLICANT' && (
            <>
              <CNavItem>
                <CNavLink
                  active={location.pathname==='/applicant/kyc'}
                  onClick={()=>navigate('/applicant/kyc')}
                  style={{cursor:'pointer'}}
                >
                  👤 KYC
                </CNavLink>
              </CNavItem>

              <CNavItem>
                <CNavLink
                  active={location.pathname==='/applicant/apply'}
                  onClick={()=>navigate('/applicant/apply')}
                  style={{cursor:'pointer'}}
                >
                  💰 Apply for Loan
                </CNavLink>
              </CNavItem>

              <CNavItem>
                <CNavLink
                  active={location.pathname==='/applicant/my-applications'}
                  onClick={()=>navigate('/applicant/my-applications')}
                  style={{cursor:'pointer'}}
                >
                  📄 My Applications
                </CNavLink>
              </CNavItem>
            </>
          )}

          {role==='LOAN_OFFICER' && (
            <CNavItem>
              <CNavLink
                active={location.pathname==='/officer/applications'}
                onClick={()=>navigate('/officer/applications')}
                style={{cursor:'pointer'}}
              >
                📋 Loan Applications
              </CNavLink>
            </CNavItem>
          )}

          {role==='MANAGER' && (
            <CNavItem>
              <CNavLink
                active={location.pathname==='/manager/approvals'}
                onClick={()=>navigate('/manager/approvals')}
                style={{cursor:'pointer'}}
              >
                ✅ Loan Approvals
              </CNavLink>
            </CNavItem>
          )}

          {role==='ADMIN' && (
            <CNavItem>
              <CNavLink
                active={location.pathname==='/admin/administration'}
                onClick={()=>navigate('/admin/administration')}
                style={{cursor:'pointer'}}
              >
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

          <CButton
            color="danger"
            variant="outline"
            size="sm"
            className="w-100"
            onClick={logout}
          >
            Logout
          </CButton>

        </div>

        <CSidebarToggler
          className="d-none d-lg-flex"
          onClick={()=>setSidebarShow(!sidebarShow)}
        />

      </CSidebar>

      <div
        className="wrapper flex-grow-1"
        style={{
          marginLeft:sidebarShow?'256px':'0',
          transition:'margin-left 0.2s'
        }}
      >

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