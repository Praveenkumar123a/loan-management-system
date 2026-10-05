import { useState } from 'react';
import { useNavigate,Link } from 'react-router-dom';
import { Container,Card,Form,Button,Alert,Navbar } from 'react-bootstrap';
import api from '../../api/axiosConfig';

function Login(){
  const [email,setEmail]=useState('');
  const [password,setPassword]=useState('');
  const [error,setError]=useState('');
  const [loading,setLoading]=useState(false);
  const navigate=useNavigate();

  const handleSubmit=async e=>{
    e.preventDefault();setError('');setLoading(true);
    try{
      const r=await api.post('/auth/login',{email,password});
      const user=r.data;
      localStorage.setItem('user',JSON.stringify(user));
      if(user.role==='APPLICANT')navigate('/applicant/dashboard');
      else if(user.role==='LOAN_OFFICER')navigate('/officer/dashboard');
      else if(user.role==='MANAGER')navigate('/manager/dashboard');
      else if(user.role==='ADMIN')navigate('/admin/dashboard');
      else navigate('/login');
    }catch(e){setError(e.response?.data?.message||'Invalid email or password.');}
    finally{setLoading(false);}
  };

  return <>
    <Navbar bg="dark" variant="dark"><Container><Navbar.Brand>Loan Management System</Navbar.Brand></Container></Navbar>
    <Container className="d-flex justify-content-center align-items-center" style={{minHeight:'85vh'}}>
      <Card className="shadow" style={{width:'100%',maxWidth:420}}>
        <Card.Body className="p-4">
          <h3 className="text-center mb-4">Login</h3>
          {error&&<Alert variant="danger">{error}</Alert>}
          <Form onSubmit={handleSubmit}>
            <Form.Group className="mb-3">
              <Form.Label>Email</Form.Label>
              <Form.Control type="email" value={email} onChange={e=>setEmail(e.target.value)} placeholder="Enter email" required/>
            </Form.Group>
            <Form.Group className="mb-3">
              <Form.Label>Password</Form.Label>
              <Form.Control type="password" value={password} onChange={e=>setPassword(e.target.value)} placeholder="Enter password" required/>
            </Form.Group>
            <Button type="submit" variant="primary" className="w-100" disabled={loading}>{loading?'Logging in...':'Login'}</Button>
          </Form>
          <p className="text-center mt-3 mb-0">Don't have an account? <Link to="/register">Register</Link></p>
        </Card.Body>
      </Card>
    </Container>
  </>;
}

export default Login;