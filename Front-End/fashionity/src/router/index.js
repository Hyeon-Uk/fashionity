import { createRouter, createWebHistory } from "vue-router";
import ProfilePage from "../components/pages/user/Profile.vue";
import ProfileView from "../views/ProfileView.vue";
import UserView from "../views/UserView.vue";
import UserRegister from "../components/pages/user/UserRegister.vue";
import UserLogin from "../components/pages/user/UserLogin.vue";

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: "/profile",
      name: "profileView",
      component: ProfileView,
      children: [
        {
          path: "",
          name: "ProfilePage",
          component: ProfilePage,
        },
      ],
    },
    {
      path: "/user",
      name: "userView",
      component: UserView,
      children: [
        {
          path: "register",
          name: "UserRegister",
          component: UserRegister,
        },
        {
          path: "login",
          name: "UserLogin",
          component: UserLogin,
        },
      ],
    },
  ],
});

export default router;
