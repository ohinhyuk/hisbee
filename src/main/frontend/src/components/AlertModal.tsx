import { motion } from "framer-motion";
import { useSetRecoilState } from "recoil";
import { isAlertModalState } from "./atom";
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

export default function AlertModal({ text }: any) {
  const setAlertModal = useSetRecoilState(isAlertModalState);

  const onClick = async (event: any) => {
    if (event.currentTarget.id === "yes") {
      setAlertModal(false);
    }
  };
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
      <div className=" modal fixed z-20 flex justify-center w-full ">
        <div className="w-[400px] rounded-2xl bg-[white] h-[250px] py-[30px] px-[30px] flex-col">
          <span className="flex justify-end ">
            <i
              onClick={onClick}
              id="no"
              className="fa-solid fa-xmark text-[20px] "
            ></i>
          </span>
          <span className="flex justify-center  ">
            <span className="flex flex-col w-[300px] items-center">
              <img className="w-[120px]" src="/img/logo_hisbee.png " />
              <p className="text-[20px] my-[40px]">{text}</p>
            </span>
          </span>

          <div className="flex justify-center ">
            <span className="w-[260px] flex justify-center">
              <button
                onClick={onClick}
                id="yes"
                className="text-white bg-blue-600 w-[110px] rounded-lg px-[20px] py-[4px] text-[15px]"
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
