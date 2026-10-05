import { Navigate } from 'react-router-dom';

function PrivateRoute({ children, allowedRole }) {
  const user = JSON.parse(localStorage.getItem('user'));

  if (!user) {
    return <Navigate to="/login" />;
  }

  if (allowedRole && user.role !== allowedRole) {
    return <Navigate to="/login" />;
  }

  return children;
}

export default PrivateRoute;