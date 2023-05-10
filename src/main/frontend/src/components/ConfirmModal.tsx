import { motion } from "framer-motion";
import { useSetRecoilState } from "recoil";
import { isConfirmModalState } from "./atom";
const LayoutVariant = {
  hidden: {
    opacity: 0,
    // backGroundColor: "rgba(0,0,0,0.5)",
  },
  showing: {
    opacity: 1,
  },
  exit: {
    opacity: 0,
  },
};

export default function ConfirmModal({ text, onClick }: any) {
  const convertedText = text?.split(";");
  const setConfirmModal = useSetRecoilState(isConfirmModalState);

  return (
    <div>
      <motion.div
        onClick={onClick}
        variants={LayoutVariant}
        initial="hidden"
        animate="showing"
        exit="exit"
        id="no"
        className="z-10 bg-[rgba(0,0,0,0.5)] fixed top-0 left-0 w-full h-screen opacity-100"
      ></motion.div>
      <div className="modal fixed z-20 flex justify-center w-full ">
        <div className="w-[400px] bg-[white] h-[250px] py-[30px] px-[30px] flex-col">
          <span className="flex justify-between ">
            <div className="w-[10px]"></div>

            <img className="w-[120px]" src="/img/logo_hisbee.png " />
            <i
              onClick={onClick}
              id="no"
              className="fa-solid fa-xmark text-[20px] "
            ></i>
          </span>
          <span className="flex justify-center  ">
            <span className="flex flex-col w-[300px] items-center">
              <p className="text-[18px] mt-[40px] mb-[40px] text-center">
                {convertedText?.map((line: string, index: number) => (
                  <p className="" key={index}>
                    {line}
                  </p>
                ))}
              </p>
            </span>
          </span>

          <div className="flex justify-center ">
            <span className="w-[260px] flex justify-between">
              <button
                onClick={onClick}
                id="no"
                className="bg-gray-100 w-[110px] rounded-lg px-[20px] py-[4px] text-[15px]"
              >
                {" "}
                취소
              </button>
              <button
                onClick={onClick}
                id="yes"
                className="text-white bg-blue-600 w-[110px] rounded-lg px-[20px] text-[15px]"
              >
                확인
              </button>
            </span>
          </div>
        </div>
      </div>
    </div>
  );
}
