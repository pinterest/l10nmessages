"use strict";(self.webpackChunkl10nmessages_website=self.webpackChunkl10nmessages_website||[]).push([[72],{3905:(e,a,n)=>{n.d(a,{Zo:()=>m,kt:()=>d});var t=n(7294);function s(e,a,n){return a in e?Object.defineProperty(e,a,{value:n,enumerable:!0,configurable:!0,writable:!0}):e[a]=n,e}function r(e,a){var n=Object.keys(e);if(Object.getOwnPropertySymbols){var t=Object.getOwnPropertySymbols(e);a&&(t=t.filter((function(a){return Object.getOwnPropertyDescriptor(e,a).enumerable}))),n.push.apply(n,t)}return n}function i(e){for(var a=1;a<arguments.length;a++){var n=null!=arguments[a]?arguments[a]:{};a%2?r(Object(n),!0).forEach((function(a){s(e,a,n[a])})):Object.getOwnPropertyDescriptors?Object.defineProperties(e,Object.getOwnPropertyDescriptors(n)):r(Object(n)).forEach((function(a){Object.defineProperty(e,a,Object.getOwnPropertyDescriptor(n,a))}))}return e}function o(e,a){if(null==e)return{};var n,t,s=function(e,a){if(null==e)return{};var n,t,s={},r=Object.keys(e);for(t=0;t<r.length;t++)n=r[t],a.indexOf(n)>=0||(s[n]=e[n]);return s}(e,a);if(Object.getOwnPropertySymbols){var r=Object.getOwnPropertySymbols(e);for(t=0;t<r.length;t++)n=r[t],a.indexOf(n)>=0||Object.prototype.propertyIsEnumerable.call(e,n)&&(s[n]=e[n])}return s}var l=t.createContext({}),c=function(e){var a=t.useContext(l),n=a;return e&&(n="function"==typeof e?e(a):i(i({},a),e)),n},m=function(e){var a=c(e.components);return t.createElement(l.Provider,{value:a},e.children)},p={inlineCode:"code",wrapper:function(e){var a=e.children;return t.createElement(t.Fragment,{},a)}},u=t.forwardRef((function(e,a){var n=e.components,s=e.mdxType,r=e.originalType,l=e.parentName,m=o(e,["components","mdxType","originalType","parentName"]),u=c(n),d=s,h=u["".concat(l,".").concat(d)]||u[d]||p[d]||r;return n?t.createElement(h,i(i({ref:a},m),{},{components:n})):t.createElement(h,i({ref:a},m))}));function d(e,a){var n=arguments,s=a&&a.mdxType;if("string"==typeof e||s){var r=n.length,i=new Array(r);i[0]=u;var o={};for(var l in a)hasOwnProperty.call(a,l)&&(o[l]=a[l]);o.originalType=e,o.mdxType="string"==typeof e?e:s,i[1]=o;for(var c=2;c<r;c++)i[c]=n[c];return t.createElement.apply(null,i)}return t.createElement.apply(null,n)}u.displayName="MDXCreateElement"},1898:(e,a,n)=>{n.r(a),n.d(a,{assets:()=>l,contentTitle:()=>i,default:()=>p,frontMatter:()=>r,metadata:()=>o,toc:()=>c});var t=n(7462),s=(n(7294),n(3905));const r={title:"Caching"},i=void 0,o={unversionedId:"caching",id:"caching",title:"Caching",description:"Caching is disabled by default.",source:"@site/docs/caching.md",sourceDirName:".",slug:"/caching",permalink:"/l10nmessages/docs/caching",draft:!1,editUrl:"https://github.com/pinterest/l10nmessages/edit/main/docs/docs/caching.md",tags:[],version:"current",frontMatter:{title:"Caching"},sidebar:"docs",previous:{title:"Fluent API",permalink:"/l10nmessages/docs/fluent-api"},next:{title:"ICU4J",permalink:"/l10nmessages/docs/icu4j"}},l={},c=[{value:"Why use a cache?",id:"why-use-a-cache",level:2},{value:"Avoid",id:"avoid",level:4},{value:"Prefer",id:"prefer",level:4},{value:"Multiple messages / different scopes",id:"multiple-messages--different-scopes",level:4},{value:"Similarly with disabled cache",id:"similarly-with-disabled-cache",level:4},{value:"With cache enabled",id:"with-cache-enabled",level:4},{value:"Basic Cache",id:"basic-cache",level:2},{value:"Guava Cache",id:"guava-cache",level:2},{value:"Thread Safety",id:"thread-safety",level:2}],m={toc:c};function p(e){let{components:a,...n}=e;return(0,s.kt)("wrapper",(0,t.Z)({},m,n,{components:a,mdxType:"MDXLayout"}),(0,s.kt)("p",null,"Caching is ",(0,s.kt)("strong",{parentName:"p"},"disabled")," by default."),(0,s.kt)("p",null,"A ",(0,s.kt)("a",{parentName:"p",href:"#basic-cache"},"basic cache")," is provided for simple usages. Any other cache can be plugged in by\nimplementing the ",(0,s.kt)("inlineCode",{parentName:"p"},"MessageFormatLoadingCacheProvider")," interface, see\n",(0,s.kt)("a",{parentName:"p",href:"#guava-cache"},"Guava Cache example"),"."),(0,s.kt)("p",null,"Be aware of ",(0,s.kt)("a",{parentName:"p",href:"#thread-safety"},"thread-safety")," when using caching."),(0,s.kt)("h2",{id:"why-use-a-cache"},"Why use a cache?"),(0,s.kt)("p",null,(0,s.kt)("inlineCode",{parentName:"p"},"MessageFormat"),"s can be costly to instantiate. When performance matters, if the same message is\ngoing to be formatted over and over it is better to only instantiate it once."),(0,s.kt)("h4",{id:"avoid"},"Avoid"),(0,s.kt)("pre",null,(0,s.kt)("code",{parentName:"pre",className:"language-java"},'class Example {\n\n  {\n    for (int i = 0; i < 5; i++) {\n      System.out.println(\n          new MessageFormat("Welcome {username}!").format(ImmutableMap.of("username", "Bob")));\n    }\n  }\n}\n\n')),(0,s.kt)("h4",{id:"prefer"},"Prefer"),(0,s.kt)("p",null,"Instantiate the ",(0,s.kt)("inlineCode",{parentName:"p"},"MessageFormat")," outside of the loop and keep re-using it."),(0,s.kt)("pre",null,(0,s.kt)("code",{parentName:"pre",className:"language-java"},'class Example {\n\n  {\n    MessageFormat messageFormat = new MessageFormat("Welcome {username}!");\n    for (int i = 0; i < 5; i++) {\n      System.out.println(messageFormat.format(ImmutableMap.of("username", "Bob")));\n    }\n  }\n}\n\n')),(0,s.kt)("h4",{id:"multiple-messages--different-scopes"},"Multiple messages / different scopes"),(0,s.kt)("p",null,"Maintaining instances for multiple messages and scope can become tedious. ",(0,s.kt)("inlineCode",{parentName:"p"},"L10nMessages")," will solve\nthis with cache enabled."),(0,s.kt)("pre",null,(0,s.kt)("code",{parentName:"pre",className:"language-java"},'class Example {\n\n  {\n    ConcurrentHashMap<String, MessageFormat> messagesConcurrentHashMap = new ConcurrentHashMap<>();\n    messagesConcurrentHashMap.put("welcome_user", new MessageFormat("Welcome {username}!"));\n    messagesConcurrentHashMap.put("bye_user", new MessageFormat("Bye {username}!"));\n\n    scope1:\n    {\n      for (int i = 0; i < 5; i++) {\n        System.out.println(\n            messagesConcurrentHashMap.get("welcome_user")\n                .format(ImmutableMap.of("username", "Bob")));\n      }\n    }\n\n    scope2:\n    {\n      for (int i = 0; i < 5; i++) {\n        System.out.println(\n            messagesConcurrentHashMap.get("welcome_user")\n                .format(ImmutableMap.of("username", "Bob")));\n        System.out.println(\n            messagesConcurrentHashMap.get("bye_user").format(ImmutableMap.of("username", "Bob")));\n      }\n    }\n  }\n}\n')),(0,s.kt)("h4",{id:"similarly-with-disabled-cache"},"Similarly with disabled cache"),(0,s.kt)("p",null,"Following code will re-create the ",(0,s.kt)("inlineCode",{parentName:"p"},"MessageFormat")," in each iteration"),(0,s.kt)("pre",null,(0,s.kt)("code",{parentName:"pre",className:"language-java"},'class Example {\n\n  {\n    L10nMessages<Messages> m = L10nMessages.builder(Messages.class).build();\n\n    for (int i = 0; i < 5; i++) {\n      System.out.println(m.format(welcome_user, "username", "Bob"));\n    }\n  }\n}\n\n')),(0,s.kt)("h4",{id:"with-cache-enabled"},"With cache enabled"),(0,s.kt)("p",null,"The ",(0,s.kt)("inlineCode",{parentName:"p"},"MessageFormat"),"s will be instantiated only once, and accessing them is transparent."),(0,s.kt)("pre",null,(0,s.kt)("code",{parentName:"pre",className:"language-java"},'class Example {\n\n  {\n    L10nMessages<Messages> m =\n        L10nMessages.builder(Messages.class)\n            .messageFormatLoadingCacheProvider(CONCURRENT_HASH_MAP)\n            .build();\n\n    scope1:\n    {\n      for (int i = 0; i < 5; i++) {\n        System.out.println(m.format(welcome_user, "username", "Bob"));\n      }\n    }\n\n    scope2:\n    {\n      for (int i = 0; i < 5; i++) {\n        System.out.println(m.format(welcome_user, "username", "Bob"));\n        System.out.println(m.format(bye_user, "username", "Bob"));\n      }\n    }\n  }\n}\n\n')),(0,s.kt)("h2",{id:"basic-cache"},"Basic Cache"),(0,s.kt)("p",null,"A basic cache based on a ",(0,s.kt)("inlineCode",{parentName:"p"},"ConcurrentHashMap")," is available."),(0,s.kt)("pre",null,(0,s.kt)("code",{parentName:"pre",className:"language-java"},"class Example {\n\n  {\n    L10nMessages<Messages> m =\n        L10nMessages.builder(Messages.class)\n            .messageFormatLoadingCacheProvider(CONCURRENT_HASH_MAP)\n            .build();\n  }\n}\n")),(0,s.kt)("h2",{id:"guava-cache"},"Guava Cache"),(0,s.kt)("p",null,"This is an example how to implement the ",(0,s.kt)("inlineCode",{parentName:"p"},"MessageFormatLoadingCacheProvider")," interface with a Guava\nCache. For the cache configuration itself, check more information at\n",(0,s.kt)("a",{parentName:"p",href:"https://github.com/google/guava/wiki/CachesExplained"},"Guava Caches Explained")),(0,s.kt)("pre",null,(0,s.kt)("code",{parentName:"pre",className:"language-java"},"class Example {\n\n  {\n    L10nMessages<Messages> m =\n        L10nMessages.builder(Messages.class)\n            .messageFormatLoadingCacheProvider(\n                (locale, messageFormatProvider) ->\n                    CacheBuilder.newBuilder()\n                        .maximumSize(10000)\n                        .build(\n                            CacheLoader.from(\n                                (String message) -> messageFormatProvider.get(message, locale)))\n                        ::getUnchecked)\n            .build();\n  }\n}\n")),(0,s.kt)("h2",{id:"thread-safety"},"Thread Safety"),(0,s.kt)("p",null,"When using a cache, it is important to understand the thread-safety of the underlying\n",(0,s.kt)("inlineCode",{parentName:"p"},"MessageFormat")," before sharing the ",(0,s.kt)("inlineCode",{parentName:"p"},"L10nMessages")," instance between multiple threads."),(0,s.kt)("p",null,"Consider having 1 instance of ",(0,s.kt)("inlineCode",{parentName:"p"},"L10nMessages")," per thread, or to have some sort of synchronization\naround the MessageFormat instance."),(0,s.kt)("p",null,"From the ICU4J documentation:"),(0,s.kt)("blockquote",null,(0,s.kt)("p",{parentName:"blockquote"},"MessageFormats are not synchronized."),(0,s.kt)("p",{parentName:"blockquote"},"It is recommended to create separate format instances for each thread. If multiple threads access\na format concurrently, it must be synchronized externally.")))}p.isMDXComponent=!0}}]);