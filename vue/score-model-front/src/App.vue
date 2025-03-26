<template>
  <div class="app-container">
    <Sidebar class="sidebar" />
    <div class="main-content">
      <div class="header">
        <h1>{{ currentTitle }}</h1>
        <div class="user-info">
          <!-- Add user info here -->
        </div>
      </div>
      <div class="content">
        <router-view />
      </div>
    </div>
  </div>
</template>

<script>
import Sidebar from './components/Sidebar.vue';

export default {
  components: {
    Sidebar,
  },
};
</script>

<style lang="scss" scoped>
@import '@/styles/variables.scss';

.app-container {
  display: flex;
  min-height: 100vh;
  background-color: $background-light;

  .sidebar {
    flex-shrink: 0;
  }

  .main-content {
    flex-grow: 1;
    padding: $spacing-lg;
    
    .header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: $spacing-lg;
      padding: $spacing-md $spacing-lg;
      background: white;
      border-radius: $border-radius-md;
      box-shadow: $box-shadow-light;
      
      h1 {
        margin: 0;
        font-size: 24px;
        color: $text-primary;
      }
    }

    .content {
      background: white;
      border-radius: $border-radius-md;
      min-height: calc(100vh - 160px);
    }
  }
}
</style>
