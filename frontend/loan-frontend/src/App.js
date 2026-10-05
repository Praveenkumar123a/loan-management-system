import { BrowserRouter,Routes,Route,Navigate } from 'react-router-dom';
import Login from './pages/auth/Login';
import Register from './pages/auth/Register';
import PrivateRoute from './components/PrivateRoute';
import AppLayout from './layouts/AppLayout';

import ApplicantDashboard from './pages/applicant/Dashboard';
import Kyc from './pages/applicant/Kyc';
import ApplyLoan from './pages/applicant/ApplyLoan';
import MyApplications from './pages/applicant/MyApplications';
import ApplicationDetail from './pages/applicant/ApplicationDetail';
import EmiSchedule from './pages/applicant/EmiSchedule';
import PayEmi from './pages/applicant/PayEmi';

import OfficerDashboard from './pages/officer/Dashboard';
import ManagerDashboard from './pages/manager/Dashboard';
import AdminDashboard from './pages/admin/Dashboard';

function App(){
  return (
    <BrowserRouter>
      <Routes>

        <Route path="/login" element={<Login/>}/>
        <Route path="/register" element={<Register/>}/>

        <Route path="/applicant/dashboard" element={
          <PrivateRoute allowedRole="APPLICANT">
            <AppLayout><ApplicantDashboard/></AppLayout>
          </PrivateRoute>
        }/>

        <Route path="/applicant/kyc" element={
          <PrivateRoute allowedRole="APPLICANT">
            <AppLayout><Kyc/></AppLayout>
          </PrivateRoute>
        }/>

        <Route path="/applicant/apply" element={
          <PrivateRoute allowedRole="APPLICANT">
            <AppLayout><ApplyLoan/></AppLayout>
          </PrivateRoute>
        }/>

        <Route path="/applicant/my-applications" element={
          <PrivateRoute allowedRole="APPLICANT">
            <AppLayout><MyApplications/></AppLayout>
          </PrivateRoute>
        }/>

        <Route path="/applicant/application/:applicationId" element={
          <PrivateRoute allowedRole="APPLICANT">
            <AppLayout><ApplicationDetail/></AppLayout>
          </PrivateRoute>
        }/>

        <Route path="/applicant/application/:applicationId/emi" element={
          <PrivateRoute allowedRole="APPLICANT">
            <AppLayout><EmiSchedule/></AppLayout>
          </PrivateRoute>
        }/>

        <Route path="/applicant/pay/:emiId" element={
          <PrivateRoute allowedRole="APPLICANT">
            <AppLayout><PayEmi/></AppLayout>
          </PrivateRoute>
        }/>

        <Route path="/officer/dashboard" element={
          <PrivateRoute allowedRole="LOAN_OFFICER">
            <AppLayout><OfficerDashboard/></AppLayout>
          </PrivateRoute>
        }/>

        <Route path="/manager/dashboard" element={
          <PrivateRoute allowedRole="MANAGER">
            <AppLayout><ManagerDashboard/></AppLayout>
          </PrivateRoute>
        }/>

        <Route path="/admin/dashboard" element={
          <PrivateRoute allowedRole="ADMIN">
            <AppLayout><AdminDashboard/></AppLayout>
          </PrivateRoute>
        }/>

        <Route path="*" element={<Navigate to="/login" replace/>}/>

      </Routes>
    </BrowserRouter>
  );
}

export default App;