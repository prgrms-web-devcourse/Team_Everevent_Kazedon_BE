# 세상의 모든 이벤트를 한 곳에, 에브리벤트

_이벤트 쿠폰 발급 / 조회 플랫폼_

![Thumbnail](https://images.velog.io/images/young_pallete/post/6f0f4cc9-bcde-4bf6-b625-4463cad72b04/Desktop%20-%201.png)

## 🖼️ 기획 배경 및 동기
### **기획 동기 - "Every + Event = Eventry"**

> **"어떻게 하면 사업자에게는 홍보를, 사용자에게 일상의 행복을 줄 수 있을까?"**

- **통 크게 이벤트를 마련해도 사람들이 오지 않는다니. 고민이 깊어지는 사업자.**
- **한편으로 할인 이벤트를 보자니, 궁금하면서도 괜찮은 곳인지 걱정하는 이용자.**

일상에 존재하는 모든 소소한 이벤트들을 모두 모아 놓은, 그런 오프라인 이벤트 참여 애플리케이션을 기획하였습니다.

## 📻 intro

동네의 작은 가게부터 큰 프랜차이즈까지, 소소한 가게만의 이벤트를 소개합니다

나만의 단골 가게를 만들고 싶을 때!

할인하는 가게가 있는지 궁금할 떄!

무료로 가게 홍보를 하고싶을 때!

## 🔨 Skills

### Back-end

<p>
    <img src = "https://user-images.githubusercontent.com/42290273/147035259-3f24b82e-c8a2-4164-b141-ee0c5669a169.png" alt="spring boot" width=17%/>
    <img src = "https://user-images.githubusercontent.com/42290273/147035440-0f90b09e-e70f-42e8-8a66-f68c78af55e0.png" alt="spring security" width=17%/>
    <img src = "https://user-images.githubusercontent.com/42290273/147035893-55541626-7ba8-4f72-ba86-361dac381e81.png" alt="jacoco" width=17%/>
    <img src = "https://user-images.githubusercontent.com/42290273/147036279-e820cd90-355a-420a-a061-a7fc5aea009b.png" alt="spring data jpa" width=17%/>
</p>

```
- SpringBoot : 2.6.1
- Java 16
- Security, Spring Data JPA
- Test 도구 : jacoco
```

### DB
<p>
    <img src="https://user-images.githubusercontent.com/42290273/147041144-b6ee60ec-4066-4190-9778-d4be42c5845f.png" alt="h2" width=15%/>
    <img src="https://user-images.githubusercontent.com/42290273/147041169-ff59db31-425d-4371-a98b-130c0fdd41bc.png" alt="mysql" width=22.3%/>
</p>

```
- DB : Mysql(배포), H2(테스트)
```

### CI/CD

<p>
    <img src = "https://user-images.githubusercontent.com/42290273/147037065-03e82b9c-29d1-49b3-be37-cb9e84e2c8c3.png" alt="github action" width=17.5%/>
    <img src = "https://user-images.githubusercontent.com/42290273/147036826-4efeb7e2-d86b-4c5f-bc64-2b26ebcaa091.png" alt="codedeploy" width=17%/>
    <img src = "https://user-images.githubusercontent.com/42290273/147035879-e250915c-1908-4786-bbf4-0690164c4a5c.png" alt="codacy" width=17%/>
</p>

```
- CI : Github-Action
- Convention : Codacy Test
- CD : Codedeploy
```

### Infra
<p>
    <img src = "https://user-images.githubusercontent.com/42290273/147036915-913a1f07-0e36-46db-8e0e-610269e528cd.png" alt="docker" width=18.4%/>
    <img src = "https://user-images.githubusercontent.com/42290273/147036808-fe5ab546-9336-4edc-8524-9b9f80f22da7.png" alt="ec2" width=16.5%/>
    <img src = "https://user-images.githubusercontent.com/42290273/147037158-e280da57-8f83-4ce3-a832-5d14a4837d0f.png" alt="s3" width=15%/>
</p>

```
- docker Image : kajedon/back
- server : AWS EC2 Ubuntu
- storage : AWS S3
```

### 협업 도구

<p>
    <img src = "https://user-images.githubusercontent.com/42290273/147037349-514396e7-06f9-4353-97e8-29c89967ef26.png" alt="jira" width=15%/>
    <img src = "https://user-images.githubusercontent.com/42290273/147037359-34f6ff0d-de2e-42c7-8c3b-66566924c3f2.png" alt="notion" width=15%/>
    <img src = "https://user-images.githubusercontent.com/42290273/147037385-f13d2d03-7e67-4e04-a2d0-4593aea951f1.png" alt="slack" width=15%/>
</p>

```
이슈관리 : Jira
문서 및 회의록 : Notion
의사소통 및 로그 관리: Slack
```

## 📝 구성도

![image](https://user-images.githubusercontent.com/48792627/147008888-144bea8c-77ae-4fde-bec2-fdbe7a40e5bd.png)

## ⚒️ ERD

![image](https://user-images.githubusercontent.com/48792627/147009658-aa83ad9d-6de8-449e-b86d-7f7b4666bb7a.png)



## 🎯 API

postman 문서 : [바로가기](https://documenter.getpostman.com/view/17808429/UVRBo6d8)

API 명세서 : [바로가기](https://www.notion.so/oranjik/API-62562bdc6ffc4015963fde37a6e4f620)



## 🧐 Team

|강희정|김진아|허승연|
|:---:|:---:|:---:|
|<img src="https://user-images.githubusercontent.com/70589857/140476091-8bd10656-630d-4221-b6c0-fff3393be16e.png" height="250" />|<img src="https://user-images.githubusercontent.com/48792627/147010927-d0e4cfce-3b0e-408f-90be-fd55812a2b0b.png" height="250" />|<img src="https://user-images.githubusercontent.com/48792627/147010939-fa0442f2-0540-478b-b799-bdd03bd7cdfd.png" height="250" />
