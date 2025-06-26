import { useSelector } from "react-redux";
import { Navigate, useLocation, Outlet } from "react-router-dom";
// import { selectCurrentAccessToken } from "../redux/slices/authSlice";

const RequireAuth = () => {
  const access_token = useSelector((state) => state.auth.access_token);
  const location = useLocation();

  return access_token ? (
    <Outlet />
  ) : (
    <Navigate to="/auth/login" state={{ from: location }} replace />
  );
};

export default RequireAuth;
