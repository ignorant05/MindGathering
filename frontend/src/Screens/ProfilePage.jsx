import { useNavigate } from "react-router-dom";
import React from "react";
import { useRef } from "react";
import { ToastContainer, toast } from "react-toastify";

import { setInfo, logOut } from "../redux/slices/authSlice";
import { useDispatch, useSelector } from "react-redux";

import {
  useDeleteMutation,
  useLogoutMutation,
  useUpdateMutation,
} from "../redux/slices/authApiSlice";

const ProfilePage = () => {
  const successToast = (msg) => {
    toast.success(msg, {
      position: "top-right",
      autoClose: 5000,
      hideProgressBar: false,
      closeOnClick: true,
      pauseOnHover: true,
      draggable: true,
      progress: undefined,
      theme: "colored",
    });
  };

  const errorToast = (err) => {
    toast.error(err, {
      position: "top-right",
      autoClose: 5000,
      hideProgressBar: false,
      closeOnClick: true,
      pauseOnHover: true,
      draggable: true,
      progress: undefined,
      theme: "colored",
    });
  };

  const navigate = useNavigate();
  const dispatch = useDispatch();

  const access_token = useSelector((state) => state.auth.access_token) || "";
  const user = useSelector((state) => state.auth.user) || {};

  console.log("token: ", access_token);
  console.log("user: ", user);

  const [update] = useUpdateMutation();
  const [del] = useDeleteMutation();
  const [logout] = useLogoutMutation();

  const usernameRef = useRef("");
  const emailRef = useRef("");
  const oldPasswordRef = useRef("");
  const newPasswordRef = useRef("");
  const confirmNewPasswordRef = useRef("");

  const updateCredentials = async (e) => {
    e.preventDefault();

    const newUsername = usernameRef.current?.value;
    const newEmail = emailRef.current?.value;
    const oldPassword = oldPasswordRef.current?.value;
    const newPassword = newPasswordRef.current?.value;
    const confirmNewPassword = confirmNewPasswordRef.current?.value;

    if (confirmNewPassword !== newPassword) {
      errorToast("Passwords Don't Match, try again");
      return;
    }

    const payload = {
      _id: user._id,
      username: newUsername,
      email: newEmail,
      oldPassword: oldPassword,
      newPassword: newPassword,
    };

    try {
      const updatedUser = await update(payload).unwrap();
      console.log(updatedUser);
      if (updatedUser && access_token) {
        successToast("Updated");
        dispatch(setInfo({ user: updatedUser, access_token: access_token }));
        navigate("/auth/login");
      }
    } catch (err) {
      errorToast(err?.data?.message || err.error);
    }
  };

  const deleteAccount = async () => {
    try {
      await del().unwrap();

      successToast("Deleted");
      dispatch(logOut());
      navigate("/auth/register");
    } catch (err) {
      errorToast(err?.data?.message || err.error);
    }
  };

  const loggingOut = async () => {
    try {
      const res = await logout().unwrap();
      console.log(res);
      if (res) {
        successToast("Loggged Out");
        dispatch(logOut());
        navigate("/auth/login");
      }
    } catch (err) {
      errorToast(err?.data?.message || err.error);
    }
  };

  return (
    <>
      <div className="space-y-15 my-28 min-h-fit w-full bg-gray-700 flex flex-col justify-center py-12 sm:px-6 lg:px-8">
        <div>
          <h1 className="mt-6 text-center text-3xl font-extrabold text-gray-900">
            Welcome~
          </h1>
        </div>
        <div className="grid grid-cols-2 w-full h-1/2 px-4 py-5 space-x-6">
          <div className="">
            <form
              onSubmit={updateCredentials}
              action="#"
              className="space-y-6"
              method="PATCH"
            >
              <div>
                <label
                  htmlFor="username"
                  className="block text-sm font-medium text-gray-700"
                >
                  Username
                </label>
                <div>
                  <input
                    defaultValue={user.username || "Not sat"}
                    ref={usernameRef}
                    type="text"
                    id="username"
                    name="username"
                    className="appearance-none rounded-md relative block w-full px-3 py-2 border border-gray-300 placeholder-gray-500 text-gray-900 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 focus:z-10 sm:text-sm"
                    autoComplete="true"
                    required
                  />
                </div>
              </div>
              <div>
                <label
                  htmlFor="email"
                  className="block text-sm font-medium text-gray-700"
                >
                  Email Address
                </label>
                <div>
                  <input
                    defaultValue={user.email || "Not sat"}
                    ref={emailRef}
                    type="email"
                    id="email"
                    name="email"
                    required
                    className="appearance-none rounded-md relative block w-full px-3 py-2 border border-gray-300 placeholder-gray-500 text-gray-900 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 focus:z-10 sm:text-sm"
                    autoComplete="true"
                  />
                </div>
              </div>

              <div>
                <label
                  htmlFor="old-password"
                  className="block text-sm font-medium text-gray-700"
                >
                  Old Password
                </label>
                <div>
                  <input
                    ref={oldPasswordRef}
                    type="password"
                    id="old-password"
                    name="old-password"
                    required
                    className="appearance-none rounded-md relative block w-full px-3 py-2 border border-gray-300 placeholder-gray-500 text-gray-900 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 focus:z-10 sm:text-sm"
                    autoComplete="true"
                    placeholder="Enter your old password"
                  />
                </div>
              </div>
              <div>
                <label
                  htmlFor="new-password"
                  className="block text-sm font-medium text-gray-700"
                >
                  New Password
                </label>
                <div>
                  <input
                    ref={newPasswordRef}
                    type="password"
                    id="new-password"
                    name="new-password"
                    required
                    className="appearance-none rounded-md relative block w-full px-3 py-2 border border-gray-300 placeholder-gray-500 text-gray-900 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 focus:z-10 sm:text-sm"
                    autoComplete="true"
                    placeholder="Enter your new password"
                  />
                </div>
              </div>
              <div>
                <label
                  htmlFor="verify-new-password"
                  className="block text-sm font-medium text-gray-700"
                >
                  New Password
                </label>
                <div>
                  <input
                    ref={confirmNewPasswordRef}
                    type="password"
                    id="verify-new-password"
                    name="verify-new-password"
                    required
                    className="appearance-none rounded-md relative block w-full px-3 py-2 border border-gray-300 placeholder-gray-500 text-gray-900 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 focus:z-10 sm:text-sm"
                    autoComplete="true"
                    placeholder="Re-Enter your new password"
                  />
                </div>
              </div>
              <div className="grid grid-cols-3 grid-rows-1 space-x-2">
                <div>
                  <button
                    type="submit"
                    className="group relative w-full flex justify-center py-2 px-4 border border-transparent text-sm font-bold rounded-md text-white bg-indigo-700 hover:text-indigo-700 hover:bg-white focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
                  >
                    Update
                  </button>
                  <ToastContainer
                    position="top-right"
                    autoClose={5000}
                    hideProgressBar={false}
                    newestOnTop
                    closeOnClick={false}
                    rtl={false}
                    pauseOnFocusLoss
                    draggable
                    pauseOnHover
                    theme="colored"
                  />
                </div>
                <div>
                  <button
                    onClick={deleteAccount}
                    type="button"
                    className="group relative w-full flex justify-center py-2 px-4 border border-transparent text-sm font-bold rounded-md text-white bg-red-700 hover:bg-white hover:text-red-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
                  >
                    Delete
                  </button>
                  <ToastContainer
                    position="top-right"
                    autoClose={5000}
                    hideProgressBar={false}
                    newestOnTop
                    closeOnClick={false}
                    rtl={false}
                    pauseOnFocusLoss
                    draggable
                    pauseOnHover
                    theme="colored"
                  />
                </div>
                <div>
                  <button
                    onClick={loggingOut}
                    type="button"
                    className="group relative w-full flex justify-center py-2 px-4 border border-transparent text-sm font-bold rounded-md text-white bg-green-500 hover:text-green-500 hover:bg-white focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
                  >
                    Logout
                  </button>
                  <ToastContainer
                    position="top-right"
                    autoClose={5000}
                    hideProgressBar={false}
                    newestOnTop
                    closeOnClick={false}
                    rtl={false}
                    pauseOnFocusLoss
                    draggable
                    pauseOnHover
                    theme="colored"
                  />
                </div>
              </div>
            </form>
          </div>
          <div className="flex justify-around items-center flex-col">
            <div className="mx-6 py-14 px-52 h-9/12 w-full space-y-6">
              <div className="flex items-center justify-center space-x-6">
                <div className="shrink-0">
                  <img
                    src="https://docs.material-tailwind.com/img/face-2.jpg"
                    alt="avatar"
                    class="relative inline-block h-[150px] w-[150px] !rounded-full object-cover object-center"
                  />
                </div>
              </div>
              <div>
                <label className="block">
                  <span className="sr-only">Choose profile photo</span>
                  <input
                    type="file"
                    onchange="loadFile(event)"
                    className="block w-full text-sm text-slate-500
        file:mr-4 file:py-2 file:px-4
        file:rounded-full file:border-0
        file:text-sm file:font-semibold
        file:bg-violet-50 file:text-violet-700
        hover:file:bg-violet-100
      "
                  />
                </label>
              </div>
            </div>
            <div className="mb-6">
              <span className="text-2xl font-bold">{user.username}</span>
            </div>
          </div>
        </div>
      </div>
    </>
  );
};

export default ProfilePage;
