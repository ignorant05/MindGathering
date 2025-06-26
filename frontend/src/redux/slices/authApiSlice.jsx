import { apiSlice } from '../../app/api/apiSlice';

const AUTH_URL = "/auth";

export const authApiSlice = apiSlice.injectEndpoints({
  endpoints: (builder) => ({
    register: builder.mutation({
      query: (data) => ({
        url: `${AUTH_URL}/register`,
        method: 'POST',
        body: {
          ...data
        }
      })
    }),
    login: builder.mutation({
      query: (data) => ({
        url: `${AUTH_URL}/login`,
        method: 'POST',
        body: { ...data }
      })
    }),
    logout: builder.mutation({
      query: () => ({
        url: `${AUTH_URL}/logout`,
        method: 'POST',
        credentials: "include",
      })
    }),
    delete: builder.mutation({
      query: () => ({
        url: `${AUTH_URL}/delete`,
        method: 'DELETE',
        credentials: "include",
      })
    }),
    update: builder.mutation({
      query: (data) => ({
        url: `${AUTH_URL}/update/credentials`,
        method: "PUT",
        credentials: "include",
        body: {
          ...data
        }
      })
    }),
    updatePic: builder.mutation({
      query: (data) => ({
        url: `${AUTH_URL}/update/picture`,
        method: "PUT",
        credentials: "include",
        body: {
          ...data
        }
      })
    }),
    getMe: builder.query({
      query: () => ({
        url: `${AUTH_URL}/profile`,
        method: "GET",
        credentials: "include"
      })
    }),
    getUserInfo: builder.query({
      query: (userId) => ({
        url: `${AUTH_URL}/profile/info?userId=${userId}`,
        method: "GET",
        credentials: "include"
      })
    })
  })
})

export const {
  useRegisterMutation,
  useLoginMutation,
  useGetMeQuery,
  useLogoutMutation,
  useDeleteMutation,
  useUpdateMutation,
  useUpdatePicMutation,
  useGetUserInfoQuery,
} = authApiSlice;

