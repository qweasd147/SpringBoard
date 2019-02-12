import axios from 'axios';
import cookieUtils from './cookieUtils';

const HOST_NAME = process.env.REACT_APP_SERVER_HOST || "";
//const HOST_NAME = "";

const METHOD = {
    get : 'get'
    , post : 'post'
    , put : 'put'
    , delete : 'delete'
}

export function requestGET(...params){
    const ajaxParams = handleParams(...params);
    
    return axios.get(...ajaxParams);
}

export function requestPOST(...params){
    const ajaxParams = handleParams(...params);
    
    return axios.post(...ajaxParams);
}

export function requestPUT(...params){
    const ajaxParams = handleParams(...params);
    
    return axios.put(...ajaxParams);
}

export function requestDELETE(...params){
    const ajaxParams = handleParams(...params);
    
    return axios.delete(...ajaxParams);
}

/**
 * 호스트 정보와 토큰 정보를 넣어 배열로 반환한다.
 * @param {*} apiURL 
 * @param {*} data 
 * @param {*} option 
 */
function handleParams(apiURL, data, option={}){
    addAuthFromCookie(option);

    if(data)
        return [HOST_NAME+apiURL, data, option]
    else
        return [HOST_NAME+apiURL, option]
}

/**
 * 쿠키에서 토큰을 가져와 입력한다.
 * @param {*} option 
 */
function addAuthFromCookie(option = {}){

    const authToken = cookieUtils.getCookie("Authorization");

    if(authToken){
        option.headers = option.headers || {};
        option.headers.Authorization = 'Bearer ' + authToken;
    }
}

export { METHOD }