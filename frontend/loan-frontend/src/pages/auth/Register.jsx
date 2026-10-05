import { useState } from 'react';
import { useNavigate,Link } from 'react-router-dom';
import { Container,Card,Form,Button,Alert,Navbar } from 'react-bootstrap';
import api from '../../api/axiosConfig';

function Register(){
  const [formData,setFormData]=useState({firstName:'',lastName:'',email:'',password:'',role:'APPLICANT'});
  const [error,setError]=useState('');
  const [msg,setMsg]=useState('');
  const [loading,setLoading]=useState(false);
  const navigate=useNavigate();

  const change=e=>setFormData({...formData,[e.target.name]:e.target.value});

  const register=async e=>{
    e.preventDefault();setError('');setMsg('');setLoading(true);
    try{
      await api.post('/auth/register',formData);
      setMsg('Registration successful. Redirecting to login...');
      setTimeout(()=>navigate('/login'),1000);
    }catch(e){
      console.error('Registration error:',e);
      setError(e.response?.data?.message||e.response?.data?.error||`Registration failed (${e.response?.status||'Network Error'}). Check backend console.`);
    }finally{setLoading(false);}
  };

  return <>
    <Navbar bg="dark" variant="dark"><Container><Navbar.Brand>Loan Management System</Navbar.Brand></Container></Navbar>
    <Container className="d-flex justify-content-center align-items-center" style={{minHeight:'85vh'}}>
      <Card className="shadow" style={{width:'100%',maxWidth:450}}>
        <Card.Body className="p-4">
          <h3 className="text-center mb-4">Create Account</h3>
          {error&&<Alert variant="danger">{error}</Alert>}
          {msg&&<Alert variant="success">{msg}</Alert>}
          <Form onSubmit={register}>
            <Form.Group className="mb-3"><Form.Label>First Name</Form.Label><Form.Control name="firstName" value={formData.firstName} onChange={change} required/></Form.Group>
            <Form.Group className="mb-3"><Form.Label>Last Name</Form.Label><Form.Control name="lastName" value={formData.lastName} onChange={change} required/></Form.Group>
            <Form.Group className="mb-3"><Form.Label>Email</Form.Label><Form.Control type="email" name="email" value={formData.email} onChange={change} required/></Form.Group>
            <Form.Group className="mb-3"><Form.Label>Password</Form.Label><Form.Control type="password" name="password" value={formData.password} onChange={change} required/></Form.Group>
            <Form.Group className="mb-3"><Form.Label>Role</Form.Label><Form.Select name="role" value={formData.role} onChange={change}><option value="APPLICANT">Applicant</option><option value="LOAN_OFFICER">Loan Officer</option><option value="MANAGER">Manager</option><option value="ADMIN">Admin</option></Form.Select></Form.Group>
            <Button type="submit" className="w-100" disabled={loading}>{loading?'Registering...':'Register'}</Button>
          </Form>
          <p className="text-center mt-3 mb-0">Already have an account? <Link to="/login">Login</Link></p>
        </Card.Body>
      </Card>
    </Container>
  </>;
}

export default Register;