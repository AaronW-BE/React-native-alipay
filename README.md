# React-native-alipay

This is a quite simple module for android application to use alipay auth, and payment.

## Usage

### Install
First of all, you need install this module to your react native project. Excute following command to install it in your project root directory:

` npm i react-native-alipay-android-acinfo `


### Reference in your project

``` import AliPayModule from 'react-native-alipay-android-acinfo'; ```
    
Now you can use this module in your code directly, like this:

```  AliPay.alipayAuth("{pathto}/api/autho",(e)=>{  ```

```     console.log(e);   ```

```  }); ```
        

### Methods provided
``` 1. alipayAuth  authUrl: the auth api from server,  callback: the callback function will be return the auth result from alipay app ```
``` function alipayAuth(String authUrl, Function callback)  ```

``` 2. pay() (support soon) ```
