package com.cloud.shiro.realm;import org.apache.shiro.authc.AuthenticationException;import org.apache.shiro.authc.AuthenticationInfo;import org.apache.shiro.authc.AuthenticationToken;import org.apache.shiro.authc.SimpleAuthenticationInfo;import org.apache.shiro.authz.AuthorizationInfo;import org.apache.shiro.realm.AuthorizingRealm;import org.apache.shiro.subject.PrincipalCollection;/** * 自定义realm * @Author zhouTao * @Date 2023/2/13 */public class CustomerRealm extends AuthorizingRealm {    // 授权    @Override    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {        return null;    }    // 认证    @Override    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {        // 在token中获取用户名//        String principal = (String) token.getPrincipal();//        System.out.println("用户名:" + principal + "----realm: " + this.getName());//        // 模拟根据身份信息从数据库查询//        if("christy".equals(principal)){//            // 参数说明：用户名 | 密码 | 当前realm的名字//            SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(principal,"123456",this.getName());//            return simpleAuthenticationInfo;//        }        // 获取当前登录的主题        String principal = (String) token.getPrincipal();        // 模拟数据库返回的数据        if("shiro".equals(principal)){            return new SimpleAuthenticationInfo(principal,"shiro",this.getName());        }        return null;    }}