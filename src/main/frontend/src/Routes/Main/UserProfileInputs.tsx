import { useMutation, useQuery } from "@tanstack/react-query";
import {
  ImemberSignup,
  IRandomNickname,
  majorAutoComplete,
  memberSignUp,
  randomNickname,
  validationNickname,
} from "api";
import { isExtraSignupModalState, isSignupModalState } from "components/atom";
import LoadingAnimation from "components/LoadingAnimation";
import { AnimatePresence, motion } from "framer-motion";
import React, { useEffect, useState } from "react";
import { useForm } from "react-hook-form";
import { useLocation, useNavigate } from "react-router";
import { Link } from "react-router-dom";
import { useSetRecoilState } from "recoil";
import tw from "tailwind-styled-components";
import { FunctionButton } from "components/FunctionButton";

import * as wjCore from "@grapecity/wijmo";
import * as wjcInput from "@grapecity/wijmo.react.input";

const ValidationVariant = {
  hidden: {
    y: -10,
    // color: "red",
    opacity: 0,
  },

  showing: {
    y: 0,
    opacity: 1,
  },

  exit: {
    y: 10,
    opacity: 0,
  },
};

const Info = tw.span`
text-[15px]
text-gray-400
w-[40px]
font-main

`;

const InfoInput = tw.input`
w-full
h-[35px]
text-[15px]
border-b-2
px-[0px]

`;

const InfoBox = tw.div`
`;

const onKeyPress = (e: React.KeyboardEvent<HTMLInputElement>) => {
  if (e.key === "Enter") {
    e.preventDefault();
  }
};

export default function UserProfileInputs({
  inputBgColor,
  register,
  watch,
  getValues,
  setValue,
  formState,
}: any) {
  watch(["nickname"]);
  useEffect(() => {
    const validationNickFn = async () => {
      const result = await validationNickname(getValues("nickname"));

      setIsPresent(result.isPresent);
    };
    validationNickFn();
  }, [getValues("nickname")]);
  // console.log(getValues("nickname"));
  console.log(inputBgColor);

  const [isPresent, setIsPresent] = useState<boolean>(false);

  const [showList1, setShowList1] = useState<string[] | []>([]);
  const [showList2, setShowList2] = useState<string[] | []>([]);

  useEffect(() => {
    majorAutoComplete(getValues("major1")).then((data) =>
      setShowList1(data.results)
    );
  }, [getValues("major1")]);

  useEffect(() => {
    majorAutoComplete(getValues("major2")).then((data) =>
      setShowList2(data.results)
    );
    console.log("!!");
  }, [getValues("major2")]);

  return (
    <div className="flex h-[250px] items-start">
      <InfoBox className="relative flex w-[50%] items-center">
        <Info>닉네임</Info>

        <div className={`w-[220px] px-[20px] `}>
          <InfoInput
            className={`${inputBgColor}`}
            type="text"
            id="nicknameInput"
            onKeyPress={onKeyPress}
            {...register("nickname", {
              required: "필수 항목입니다.",
              maxLength: {
                value: 10,
                message: "10자 이하만 가능합니다",
              },
            })}
            placeholder="00자 이내로 작성해주세요"
          />
        </div>
        <FunctionButton
          text="닉네임 자동생성"
          onClick={async () => {
            const newNickname: IRandomNickname = await randomNickname();
            await setValue("nickname", newNickname.words[0]);
            document.getElementById("nicknameInput")?.focus();
          }}
        />
        <AnimatePresence>
          {(formState.errors.nickname?.message as string) && (
            <motion.div
              variants={ValidationVariant}
              className={`absolute top-[38px] left-[60px] text-[13px] text-red-600 `}
              initial="hidden"
              animate="showing"
              exit="exit"
            >
              * {formState.errors.nickname?.message as string}
            </motion.div>
          )}

          {getValues("nickname") !== "" && (
            <motion.div
              variants={ValidationVariant}
              className={`absolute top-[38px] left-[60px] text-[13px] ${
                isPresent ? "text-red-600" : "text-blue-600"
              }`}
              initial="hidden"
              animate="showing"
              exit="exit"
            >
              {isPresent
                ? "* 이미 존재하는 닉네임 입니다."
                : "* 사용 가능한 닉네임입니다."}
            </motion.div>
          )}
        </AnimatePresence>
      </InfoBox>

      <InfoBox className="flex flex-col w-[50%] pl-[50px]">
        <InfoBox className=" relative flex w-full items-start mb-[10px] ">
          <Info className="mt-[5px]">1전공</Info>
          <div className={`w-[300px] px-[20px] flex flex-col`}>
            <InfoInput
              className={` ${inputBgColor} mb-[10px]`}
              type="text"
              onKeyPress={onKeyPress}
              {...register("major1", {
                required: "필수 항목입니다.",
                maxLength: {
                  value: 10,
                  message: "10자 이하만 가능합니다",
                },
              })}
              placeholder="GLS 1전공만 입력"
            />

            <select className="bg-gray-100" size={3}>
              {showList1.length !== 0 &&
                showList1.map((elem, index) => (
                  <option
                    className="bg-slate-100"
                    value={elem}
                    key={index}
                    onClick={() => setValue("major1", elem)}
                  >
                    {elem}
                  </option>
                ))}
            </select>
          </div>
        </InfoBox>
        <InfoBox className="relative flex w-full items-start">
          <Info className="mt-[5px]">2전공</Info>
          <div className={`w-full px-[20px] `}>
            <div>
              <InfoInput
                className={` ${inputBgColor}`}
                type="text"
                onKeyPress={onKeyPress}
                {...register("major2", {
                  maxLength: {
                    value: 10,
                    message: "10자 이하만 가능합니다",
                  },
                })}
              />
            </div>
            <select className="bg-gray-100 mt-[10px]" size={3}>
              {showList2.length !== 0 &&
                showList2.map((elem, index) => (
                  <option
                    className="bg-slate-100"
                    value={elem}
                    key={index}
                    onClick={() => setValue("major2", elem)}
                  >
                    {elem}
                  </option>
                ))}
            </select>
          </div>
        </InfoBox>
      </InfoBox>
    </div>
  );
}
