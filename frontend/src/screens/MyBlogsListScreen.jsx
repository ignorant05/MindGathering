import { useEffect, useState } from "react"
import { useDispatch, useSelector } from "react-redux"
import { setBlogs, setPage } from "../redux/slices/pageSlice"
import { setInfo } from "../redux/slices/authSlice"

const MyBlogsListScreen = () => {
  const dispatch = useDispatch()

  const storedToken = sessionStorage.getItem('access_token');
  const auth = useSelector((state) => state.auth);
  const access_token = auth?.access_token || storedToken || "";

  useEffect(() => {
    setInfo({ access_token })
  }, [access_token])


  const { blogsPerPage, blogs, page } = useSelector((state) => state.blogs)
  const [totalPages, setTotalPages] = useState(0);

  useEffect(() => {
    const fetchBlogs = async () => {
      const res = await fetch(`/users/blogs?page=${page}&size=10`, {
        headers: {
          Authorization: `Bearer ${access_token}`,
        }
      })

      const data = await res.json();
      const total = res.headers.get("X-Total-Count");

      setTotalPages(Number(total));
      dispatch(setBlogs(data));
    };

    fetchBlogs();
  }, [page, blogsPerPage, dispatch, access_token]);


  return (
    <>
      <div className="max-w-5xl mx-auto mt-8">
        <div className="border-l-2 border-gray-500 pl-8">
          {
            blogs.map((blog) => <BlogCard key={blog.id} body={blog} />)
          }
        </div>
      </div>
      <div className="h-screen flex justify-center items-center dark:bg-gray-800 w-full">
        <div className="flex items-center justify-center">
          <div className="py-3 border rounded-lg dark:border-gray-600">
            <ol
              className="flex items-center text-sm text-gray-500 divide-x rtl:divide-x-reverse divide-gray-300 dark:text-gray-400 dark:divide-gray-600">
              <li>
                <button type="button"
                  onClick={dispatch(setPage(page - 1))}
                  className="relative flex items-center justify-center font-medium min-w-[2rem] px-1.5 h-8 -my-3 rounded-md outline-none hover:bg-gray-500/5 focus:bg-yellow-500/10 focus:ring-2 focus:ring-yellow-500 dark:hover:bg-gray-400/5 transition text-yellow-600"
                  aria-label="Previous" rel="prev">
                  <svg className="w-5 h-5 rtl:scale-x-[-1]" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20"
                    fill="currentColor" aria-hidden="true">
                    <path fill-rule="evenodd"
                      d="M12.707 5.293a1 1 0 010 1.414L9.414 10l3.293 3.293a1 1 0 01-1.414 1.414l-4-4a1 1 0 010-1.414l4-4a1 1 0 011.414 0z"
                      clip-rule="evenodd"></path>
                  </svg>
                </button>
              </li>

              <li>
                <button type="button"
                  className="relative flex items-center justify-center font-medium min-w-[2rem] px-1.5 h-8 -my-3 rounded-md outline-none transition text-yellow-600 focus:underline bg-yellow-500/10 ring-2 ring-yellow-500">
                  <span>{page}</span>
                </button>
              </li>
              <li>
                <button type="button"
                  className="relative flex items-center justify-center font-medium min-w-[2rem] px-1.5 h-8 -my-3 rounded-md outline-none hover:bg-gray-500/5 focus:bg-yellow-500/10 focus:ring-2 focus:ring-yellow-500 dark:hover:bg-gray-400/5 focus:text-yellow-600 transition">

                  <span>{page + 1}</span>
                </button>
              </li>
              <li>
                <button disabled="" type="button"
                  className="relative flex items-center justify-center font-medium min-w-[2rem] px-1.5 h-8 -my-3 rounded-md outline-none filament-tables-pagination-item-disabled cursor-not-allowed pointer-events-none opacity-70">

                  <span>...</span>
                </button>
              </li>

              <li>
                <button type="button"
                  className="relative flex items-center justify-center font-medium min-w-[2rem] px-1.5 h-8 -my-3 rounded-md outline-none hover:bg-gray-500/5 focus:bg-yellow-500/10 focus:ring-2 focus:ring-yellow-500 dark:hover:bg-gray-400/5 focus:text-yellow-600 transition">

                  <span>{totalPages}</span>
                </button>
              </li>

              <li>
                <button type="button"
                  onClick={dispatch(setPage(page + 1))}
                  className="relative flex items-center justify-center font-medium min-w-[2rem] px-1.5 h-8 -my-3 rounded-md outline-none hover:bg-gray-500/5 focus:bg-yellow-500/10 focus:ring-2 focus:ring-yellow-500 dark:hover:bg-gray-400/5 transition text-yellow-600"
                  aria-label="Next" rel="next">
                  <svg className="w-5 h-5 rtl:scale-x-[-1]" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20"
                    fill="currentColor" aria-hidden="true">
                    <path fill-rule="evenodd"
                      d="M7.293 14.707a1 1 0 010-1.414L10.586 10 7.293 6.707a1 1 0 011.414-1.414l4 4a1 1 0 010 1.414l-4 4a1 1 0 01-1.414 0z"
                      clip-rule="evenodd"></path>
                  </svg>
                </button>
              </li>
            </ol>
          </div>
        </div>
      </div>
    </>
  )
}

export default MyBlogsListScreen
