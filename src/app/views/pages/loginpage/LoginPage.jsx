import React, { useState } from "react";
import cn from "classnames";
import { Button, CircularProgress, TextField } from "@mui/material";
import { Checkbox } from "antd";
import Services from "app/services";
import { useDispatch } from "react-redux";
import allActions from "app/actions";
import ArrowBackIcon from '@mui/icons-material/ArrowBack';
const LoginPage = () => {
    const dispatch = useDispatch();
    const [user, setUser] = useState()
    const [error, setError] = useState("")
    const [sending, setSending] = useState(false)
    const [path, setPath] = useState(new URLSearchParams(window.location.search).get("url"));
    const handleChange = (arr, value) => {
        setError("")
        setUser({ ...user, [arr]: value })
    }
    const handleLogin = () => {
        setSending(true);
        setError("")
        Services.getTaiKhoanService().dangNhap(user).then(
            (res) => {

                if (!res?.data?.error) { 
                    console.log(res?.data);
                    dispatch(allActions.taiKhoanActions.loginUser(res?.data))
                    window.location.href = path || "/"
                } else {
                    setError(res?.data?.message)
                }
                setSending(false);
            }
        )
    }

    

    return (

        <div className="container background-login">
            <div className="screen">
                <div className="screen__content">

                    <form className="login">
                        <div className="login__field">
                            <p className="bold f-35 text-center mb-4"><span  style={{color:'#DB3A34'}}  className="nowrap">CALENDAR</span></p>
                            <p className="bold f-24 ">Đăng nhập</p>
                        </div>
                        <div className="login__field">
                            <i className="login__icon fas fa-user"></i>
                            <TextField
                                onChange={(e) => handleChange("username", e?.target?.value)}
                                onKeyDown={(e) => {
                                    if (e.key === "Enter") {
                                        handleLogin()
                                    }
                                }}
                                className="inputlg"
                                id="outlined-basic" label="Email/Số điện thoại" variant="standard" placeholder="Nhập địa chỉ email/sđt" />
                        </div>
                        <div className="login__field">
                            <i className="login__icon fas fa-lock"></i>
                            <TextField
                                onChange={(e) => handleChange("password", e?.target?.value)}
                                
                                className="inputlg"
                                onKeyDown={(e) => {
                                    if (e.key === "Enter") {
                                        handleLogin()
                                    }
                                }}
                                id="outlined-basic"
                                label="Mật khẩu" type="password" variant="standard" />
                        </div>
                        <Checkbox>Lưu mật khẩu</Checkbox>
                        <br />
                        <Button  style={{ width: '100%' , backgroundColor:'#084C61'}} variant="contained" className="mt-5" disabled={sending}
                            onClick={() => {
                                handleLogin()
                            }}
                        >
                            <span style={{ display: sending ? 'inherit' : 'none' }} >
                                <CircularProgress className="span-sender" />
                            </span>
                            Đăng nhập
                        </Button>
                        <p className='red mt-3 f-13'><i className="errlg">{error}</i></p>
                    </form>
                    {/* <div className="social-login">
                        <a href="/#">Quên mật khẩu •</a>
                        <br />

                        <a href="/#">Đăng ký tài khoản •</a>
                        <br />

                        <a href="/" ><ArrowBackIcon className="f-13 me-1" />Quay lại</a>
                    </div> */}
                </div>
                <div className="screen__background">
                  
                </div>
            </div>
        </div>
    );
};

export default LoginPage;
