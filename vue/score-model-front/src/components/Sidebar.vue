<template>
  <aside class="sidebar">
    <ul>
      <li><router-link to="/hushen300">沪深300</router-link></li>
      <li><router-link to="/chuangyeban">创业板</router-link></li>
      <li><router-link to="/shuangchuang50">双创50</router-link></li>
    </ul>
  </aside>
</template>

<style>
.sidebar {
  width: 200px;
  background-color: #2c3e50;
  color: white;
  padding: 20px;
  height: 100%;
}
.sidebar ul {
  list-style: none;
  padding: 0;
}
.sidebar li {
  margin-bottom: 10px;
}
.sidebar a {
  color: white;
  text-decoration: none;
}
.sidebar a.router-link-active {
  font-weight: bold;
  text-decoration: underline;
}
</style>
