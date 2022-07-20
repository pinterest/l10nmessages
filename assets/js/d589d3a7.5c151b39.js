"use strict";(self.webpackChunkl10nmessages_website=self.webpackChunkl10nmessages_website||[]).push([[162],{3905:(e,t,n)=>{n.d(t,{Zo:()=>c,kt:()=>g});var a=n(7294);function r(e,t,n){return t in e?Object.defineProperty(e,t,{value:n,enumerable:!0,configurable:!0,writable:!0}):e[t]=n,e}function s(e,t){var n=Object.keys(e);if(Object.getOwnPropertySymbols){var a=Object.getOwnPropertySymbols(e);t&&(a=a.filter((function(t){return Object.getOwnPropertyDescriptor(e,t).enumerable}))),n.push.apply(n,a)}return n}function i(e){for(var t=1;t<arguments.length;t++){var n=null!=arguments[t]?arguments[t]:{};t%2?s(Object(n),!0).forEach((function(t){r(e,t,n[t])})):Object.getOwnPropertyDescriptors?Object.defineProperties(e,Object.getOwnPropertyDescriptors(n)):s(Object(n)).forEach((function(t){Object.defineProperty(e,t,Object.getOwnPropertyDescriptor(n,t))}))}return e}function o(e,t){if(null==e)return{};var n,a,r=function(e,t){if(null==e)return{};var n,a,r={},s=Object.keys(e);for(a=0;a<s.length;a++)n=s[a],t.indexOf(n)>=0||(r[n]=e[n]);return r}(e,t);if(Object.getOwnPropertySymbols){var s=Object.getOwnPropertySymbols(e);for(a=0;a<s.length;a++)n=s[a],t.indexOf(n)>=0||Object.prototype.propertyIsEnumerable.call(e,n)&&(r[n]=e[n])}return r}var l=a.createContext({}),p=function(e){var t=a.useContext(l),n=t;return e&&(n="function"==typeof e?e(t):i(i({},t),e)),n},c=function(e){var t=p(e.components);return a.createElement(l.Provider,{value:t},e.children)},m={inlineCode:"code",wrapper:function(e){var t=e.children;return a.createElement(a.Fragment,{},t)}},u=a.forwardRef((function(e,t){var n=e.components,r=e.mdxType,s=e.originalType,l=e.parentName,c=o(e,["components","mdxType","originalType","parentName"]),u=p(n),g=r,d=u["".concat(l,".").concat(g)]||u[g]||m[g]||s;return n?a.createElement(d,i(i({ref:t},c),{},{components:n})):a.createElement(d,i({ref:t},c))}));function g(e,t){var n=arguments,r=t&&t.mdxType;if("string"==typeof e||r){var s=n.length,i=new Array(s);i[0]=u;var o={};for(var l in t)hasOwnProperty.call(t,l)&&(o[l]=t[l]);o.originalType=e,o.mdxType="string"==typeof e?e:r,i[1]=o;for(var p=2;p<s;p++)i[p]=n[p];return a.createElement.apply(null,i)}return a.createElement.apply(null,n)}u.displayName="MDXCreateElement"},9390:(e,t,n)=>{n.r(t),n.d(t,{assets:()=>l,contentTitle:()=>i,default:()=>m,frontMatter:()=>s,metadata:()=>o,toc:()=>p});var a=n(7462),r=(n(7294),n(3905));const s={title:"Getting Started"},i=void 0,o={unversionedId:"getting-started",id:"getting-started",title:"Getting Started",description:"See the installation guide for your build system or start with",source:"@site/docs/getting-started.md",sourceDirName:".",slug:"/getting-started",permalink:"/docs/getting-started",draft:!1,editUrl:"https://github.com/pinterest/l10nmessages/edit/docs/docs/getting-started.md",tags:[],version:"current",frontMatter:{title:"Getting Started"},sidebar:"docs",previous:{title:"Introduction",permalink:"/docs/introduction"},next:{title:"Installation",permalink:"/docs/installation/"}},l={},p=[{value:"Create a resource bundle",id:"create-a-resource-bundle",level:2},{value:"Register the resource bundle with <code>@L10nProperties</code>",id:"register-the-resource-bundle-with-l10nproperties",level:2},{value:"Enum generated by the annotation processor",id:"enum-generated-by-the-annotation-processor",level:2},{value:"Strong typing using Enum",id:"strong-typing-using-enum",level:2},{value:"Localization",id:"localization",level:2}],c={toc:p};function m(e){let{components:t,...n}=e;return(0,r.kt)("wrapper",(0,a.Z)({},c,n,{components:t,mdxType:"MDXLayout"}),(0,r.kt)("p",null,"See the ",(0,r.kt)("a",{parentName:"p",href:"installation"},"installation")," guide for your build system or start with\n",(0,r.kt)("a",{parentName:"p",href:"examples"},"existing examples"),"."),(0,r.kt)("p",null,"This guide goes through all the steps required to render a localized message in a Java application\nstarting from scratch."),(0,r.kt)("h2",{id:"create-a-resource-bundle"},"Create a resource bundle"),(0,r.kt)("p",null,"Create a root file: ",(0,r.kt)("inlineCode",{parentName:"p"},"Messages.properties")," in the ",(0,r.kt)("inlineCode",{parentName:"p"},"com.pinterest.l10nmessages.example")," package."),(0,r.kt)("p",null,"The corresponding resource bundle ",(0,r.kt)("inlineCode",{parentName:"p"},"baseName")," is ",(0,r.kt)("inlineCode",{parentName:"p"},"com.pinterest.l10nmessages.example.Messages"),"."),(0,r.kt)("p",null,(0,r.kt)("inlineCode",{parentName:"p"},"UTF-8")," is the recommended encoding for the ",(0,r.kt)("inlineCode",{parentName:"p"},"properties")," files."),(0,r.kt)("p",null,"In a project that follows the Maven layout, the file would be:"),(0,r.kt)("pre",null,(0,r.kt)("code",{parentName:"pre",className:"language-properties",metastring:'title="src/resources/java/com/pinterest/l10nmessages/example/Messages.properties"',title:'"src/resources/java/com/pinterest/l10nmessages/example/Messages.properties"'},"welcome_user=Welcome {username}!\n")),(0,r.kt)("h2",{id:"register-the-resource-bundle-with-l10nproperties"},"Register the resource bundle with ",(0,r.kt)("inlineCode",{parentName:"h2"},"@L10nProperties")),(0,r.kt)("p",null,"Add the ",(0,r.kt)("inlineCode",{parentName:"p"},"@L10nProperties")," annotation to the application class to register the resource bundle with\nthe annotation processor."),(0,r.kt)("pre",null,(0,r.kt)("code",{parentName:"pre",className:"language-java",metastring:'title="src/main/java/com/pinterest/l10nmessages/example/Application.java"',title:'"src/main/java/com/pinterest/l10nmessages/example/Application.java"'},'import com.pinterest.l10nmessages.L10nProperties;\n\n@L10nProperties(baseName = "com.pinterest.l10nmessages.example.Messages")\npublic class Application {\n\n}\n')),(0,r.kt)("h2",{id:"enum-generated-by-the-annotation-processor"},"Enum generated by the annotation processor"),(0,r.kt)("p",null,"Compile your project. The annotation processor should generate the following ",(0,r.kt)("inlineCode",{parentName:"p"},"enum"),":"),(0,r.kt)("pre",null,(0,r.kt)("code",{parentName:"pre",className:"language-java",metastring:'title="target/generated-sources/annotations/com/pinterest/l10nmessages/example/Messages.java"',title:'"target/generated-sources/annotations/com/pinterest/l10nmessages/example/Messages.java"'},'package com.pinterest.l10nmessages.example;\n\npublic enum Messages {\n  welcome_user("welcome_user");\n\n  public static final String BASENAME = "com.pinterest.l10nmessages.example.Messages";\n  // ...\n}\n')),(0,r.kt)("h2",{id:"strong-typing-using-enum"},"Strong typing using Enum"),(0,r.kt)("p",null,"That ",(0,r.kt)("inlineCode",{parentName:"p"},"enum")," can be used to create the ",(0,r.kt)("inlineCode",{parentName:"p"},"L10nMessages")," instance and then to format a message using the\ntyped key: ",(0,r.kt)("inlineCode",{parentName:"p"},"Messages.welcome_user"),". The argument: ",(0,r.kt)("inlineCode",{parentName:"p"},"username")," is provided as a key/value pair."),(0,r.kt)("pre",null,(0,r.kt)("code",{parentName:"pre",className:"language-java",metastring:'title="src/main/java/com/pinterest/l10nmessages/example/Application.java"',title:'"src/main/java/com/pinterest/l10nmessages/example/Application.java"'},'import com.pinterest.l10nmessages.L10nMessages;\nimport com.pinterest.l10nmessages.L10nProperties;\n\n@L10nProperties(baseName = "com.pinterest.l10nmessages.example.Messages")\npublic class Application {\n\n  public static void main(String[] args) {\n    L10nMessages<Messages> m = L10nMessages.builder(Messages.class).build();\n    String localizedMsg = m.format(Messages.welcome_user, "username", "Mary");\n    System.out.println(localizedMsg);\n    // Welcome Mary!\n  }\n}\n')),(0,r.kt)("p",null,"For extra typing, consider ",(0,r.kt)("a",{parentName:"p",href:"fluent-api#argument-names-typing"},"argument names typing"),"."),(0,r.kt)("h2",{id:"localization"},"Localization"),(0,r.kt)("p",null,"Localize the root properties file by creating the file ",(0,r.kt)("inlineCode",{parentName:"p"},"Messages_fr.properties"),' for "French"'),(0,r.kt)("pre",null,(0,r.kt)("code",{parentName:"pre",className:"language-properties"},"welcome_user=Bienvenue {username}!\n")),(0,r.kt)("p",null,"Specify the locale wanted for the messages"),(0,r.kt)("pre",null,(0,r.kt)("code",{parentName:"pre",className:"language-java"},'\n@L10nProperties(baseName = "com.pinterest.l10nmessages.example.Messages")\npublic class Application {\n\n  public static void main(String[] args) {\n    L10nMessages<Messages> m = L10nMessages.builder(Messages.class)\n        .locale(Locale.FRENCH)\n        .build();\n    String localizedMsg = m.format(Messages.welcome_user, "username", "Mary");\n    System.out.prinln(localizedMsg);\n    // Bienvenue Mary!\n  }\n}\n')),(0,r.kt)("p",null,"For advanced message formatting and localization, see the ",(0,r.kt)("a",{parentName:"p",href:"icu4j"},"ICU4J guide"),"."))}m.isMDXComponent=!0}}]);