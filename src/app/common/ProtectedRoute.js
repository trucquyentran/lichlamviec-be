import { Navigate, useLocation } from "react-router-dom";

const ProtectedRoute = ({ user, role, redirectPath = '/dang-nhap', children }) => {


  console.log(role)
  console.log(user.listQuyen.includes(role))


  if (user?.listQuyen?.includes("User") || user?.listQuyen?.includes("Admin")) {
    return children;
  } 
  return <Navigate to={redirectPath + "?url=" + window.location.pathname} replace />;

};

export default ProtectedRoute;