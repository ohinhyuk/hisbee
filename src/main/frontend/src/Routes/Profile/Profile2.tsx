import { useQuery } from "@tanstack/react-query";
import { IOneUser, readOneMember } from "api";
import { isDeleteModalState } from "components/atom";
import { FunctionButton } from "components/FunctionButton";
import { useState } from "react";
import { useForm } from "react-hook-form";
import { Link } from "react-router-dom";
import { useRecoilState } from "recoil";
import UserProfileInputs from "Routes/Main/UserProfileInputs";
import { PostExamples } from "Routes/PostAddForm/components/PostExamples";
import tw from "tailwind-styled-components";
import DeleteModal from "./DeleteModal";
import Thumbnail from "./Thumbnail";

const PostGrid = tw.div`
grid
grid-cols-1
md:grid-cols-2
lg:grid-cols-3
xl:grid-cols-4
gap-x-10
px-5
place-items-center

`;

const ProfileBlock = tw.div`

`;
const ProfileInfoBox = tw.div`
flex 

w-[230px]

`;

const ProfileInfoTitle = tw.p`
w-[150px] 
text-gray-500
font-medium
`;

const ProfileInfoIcon = tw.i`
text-gray-600
mr-2
`;

const Sidebar = tw.div`
sticky
top-0
hidden
lg:flex
min-w-[220px] 
pl-[30px]

h-[00px]
min-h-screen
flex-col
items-start

`;

const SidebarButton = tw.button`
flex
justify-start
items-center
my-[5px]
pl-[5px]
pr-[40px]
w-[180px]
py-[8px]
rounded-lg
align-left
text-gray-400
text-[16px]

hover:text-gray-500
hover:bg-gray-100
`;

const SidebarTitle = tw.p`
py-[20px] 
text-[20px] 
`;

const Banner = tw.div`
h-[300px] bg-slate-100 flex
`;
function Profile2() {
  //   const {
  //     isLoading: getUserLoading,
  //     data,
  //     refetch,
  //   } = useQuery<IOneUser>(["User"], readOneMember, {
  //     onSuccess: async (data) => {
  //       setValue("nickname", data.nickname);
  //       setValue("major1", data.major1);
  //       setValue("major2", data.major2);

  //       //   setOnSuccessLoading(false);
  //     },
  //     onError: (error) => {
  //       //   if (((error as AxiosError).response as AxiosResponse).status === 401) {
  //       // alert("로그인이 필요합니다.");
  //       // setIsLoginModal(true);
  //       // setIsLogin(false);
  //       // if (localStorage.getItem("key")) localStorage.removeItem("key");
  //       // navigate("/");
  //       //   }
  //     },
  //   });

  const { register, handleSubmit, formState, setValue, getValues, watch } =
    useForm({
      defaultValues: {
        nickname: "따뜻한 붕어빵",
        major1: "제품디자인",
        major2: "시각디자인",
      },
    });

  const [modifyToggle, setModifyToggle] = useState<boolean>(false);

  const onProfileModify = () => {
    setModifyToggle(true);
  };

  const [isDeleteModal, setIsDeleteModal] = useRecoilState(isDeleteModalState);

  const onDeleteClick = (event: React.FormEvent<HTMLButtonElement>) => {
    event.preventDefault();
    setIsDeleteModal(true);
  };

  const [mode, setMode] = useState("like");

  return (
    <>
      {isDeleteModal && <DeleteModal />}
      <div className="w-full flex">
        <Sidebar>
          <SidebarTitle className="relative">마이 페이지</SidebarTitle>

          <SidebarButton
            className={` ${mode === "like" && "bg-blue-100 text-blue-600"}`}
            onClick={() => setMode("like")}
          >
            <i className="fa-regular fa-heart mx-[10px]"></i>
            좋아요한 글
          </SidebarButton>

          <SidebarButton
            className={` ${mode === "mine" && "bg-blue-100 text-blue-600"}`}
            onClick={() => setMode("mine")}
          >
            <i className="fa-solid fa-pencil mx-[10px] "></i>
            작성한 글
          </SidebarButton>
          <SidebarButton
            onClick={onDeleteClick}
            className="absolute top-[600px] hover:bg-red-100 hover:text-red-500"
          >
            <i className="fa-solid fa-user-slash mx-[10px]"></i>탈퇴하기
          </SidebarButton>
        </Sidebar>
        <div className="w-full h-[1300px] ">
          <Banner className="flex justify-center items-center">
            <div className="flex h-[150px] w-full px-[100px] py-[10px]">
              <div>
                <img src="./img/user.png" className="mr-[100px] w-[100px]" />
              </div>
              <div className="flex flex-col   ">
                {modifyToggle ? (
                  <>
                    <UserProfileInputs
                      watch={watch}
                      register={register}
                      getValues={getValues}
                      setValue={setValue}
                      formState={formState}
                      inputBgColor="bg-slate-100"
                    />
                    <div className="flex justify-end mt-[20px] ">
                      <button
                        type="button"
                        onClick={(e) => {
                          e.preventDefault();

                          //   refetch()
                          setModifyToggle(false);
                        }}
                        className="bg-red-100 text-red-400 rounded-lg px-[20px] py-[7px] text-[13px] mr-[10px]"
                      >
                        편집 취소
                      </button>
                      <button
                        type="button"
                        onClick={() => {
                          // await memberUpdate(newUser as any);
                          // setNowModifying(false);
                          // refetch();
                        }}
                        className="bg-blue-500 text-white rounded-lg px-[20px] py-[7px] text-[13px]"
                      >
                        편집 완료
                      </button>
                    </div>
                  </>
                ) : (
                  <>
                    <div className="flex justify-start">
                      <div className=" mr-[40px]">{getValues("nickname")}</div>
                      <FunctionButton
                        text="프로필 편집"
                        onClick={onProfileModify}
                      ></FunctionButton>
                    </div>
                    <div>
                      <div className=" text-gray-500 mt-[10px]">
                        {getValues("major1") as any}
                        {getValues("major2") !== "" &&
                          getValues("major2") !== null &&
                          " / " + getValues("major2")}
                      </div>
                    </div>
                  </>
                )}
              </div>
            </div>
          </Banner>

          {/* 글 가져 오기 */}
          <PostGrid>
            {/* {posts?.posts?.map((post: IReadOnePost, index: number) => (
          <Link key={index} to={`/post2/${post?.id}`}>
            <Thumbnail {...post} refetch={refetch} />
          </Link>
        ))} */}
          </PostGrid>
        </div>
      </div>
    </>
  );
}

export default Profile2;