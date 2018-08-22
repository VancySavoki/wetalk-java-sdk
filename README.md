Wetalk-JAVA-SDK
------
方便企业内部应用利用微信企业号和钉钉能力开发的sdk。可结合Spring Boot。
* 使用统一OAuth2接口认证员工信息
* 方便使用统一消息接口给员工发送消息和文件  

#### 示例
[查看完整java后端示例](/example/AuthController.java)  
[查看完整前端示例](/example/api.js)

#### 概念
前后端都采用Provider的概念编写，用伪代码表示
```java
class ProviderManager {
    Array providers;    
}
ProviderManager providerManager;
Condition ua;
PreAuthentication preAuthentication;
for(Provider provider: providerManager.providers) {
    final Support supporter = (Support) provider;
    if (supporter.support(ua)) {
        preAuthentication = supporter.getPreAuthenticationToken(code);
        break;
    }
    providerManager.authenticate(preAuthentication);
}
```
思想是即通过数组管理认证器，利用环境参数去遍历认证器是否可用，可用即中断并使用该认证器进行认证。