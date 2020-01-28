<template>
  <div id="app">
<!--    <img alt="Vue logo" src="./assets/logo.png">-->
    <div class="row text-center">
      <div id="header" class="col-sm-12">Zenorger</div>
    </div>
    <div class="row">
      <div class="col-sm-2">
        <button class="btn btn-link"
                v-bind:class="{ 'btn-outline-primary': this.selectedListCode === 'Inbox' }"
                v-on:click="showTasks('Inbox')">Inbox
        </button>
        <div>Contexts</div>
        <ul>
          <li v-for="context in contexts" :key="context.id">
            <button class="btn btn-link"
                    v-bind:class="{ 'btn-outline-primary': context.id === this.selectedListCode }"
                    v-on:click="showTasks(context.id)">{{context.name}}
            </button>
          </li>
        </ul>
        <button class="btn btn-link"
                v-bind:class="{ 'btn-outline-primary': this.selectedListCode === 'Pending' }"
                v-on:click="showTasks('Pending')">Pending
        </button>
        <button class="btn btn-link"
                v-bind:class="{ 'btn-outline-primary': this.selectedListCode === 'SomedayMaybe' }"
                v-on:click="showTasks('SomedayMaybe')">Someday / Maybe
        </button>
        <button class="btn btn-link"
                v-bind:class="{ 'btn-outline-primary': this.selectedListCode === 'Removed' }"
                v-on:click="showTasks('Removed')">Trash
        </button>
      </div>
      <div id="taskList" class="col-sm-5">
        <div id="addTask" class="row">
          <div class="col-sm-12">
            <div class="form">
              <div>
                <input type="text" class="form-control" v-model="this.newTaskName" placeholder="Add task..."/>
              </div>
              <button class="btn btn-primary form-group"
                      v-bind:disabled="!this.newTaskName"
                      v-on:click="this.addTask(this.newTaskName)">Add task
              </button>
            </div>
          </div>
        </div>
        <div class="block">
          <table class="table table-sm table-hover">
            <tbody>
              <tr v-for="task in this.tasks" :key="task.id">
                <td v-bind:class="{ 'table-success': task.id === this.editableTask.id }"
                    v-on:click="this.showTask(task)">
                  <span>{{task.name}}</span>
                  <span class="float-right">{{task.completeDate}}</span>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
      <div id="taskForm" class="col-sm-5">
        <div class="form" v-if="this.isTaskEdit">
          <!-- Name -->
          <input v-model.lazy="this.editableTask.name" type="text" class="form-control" placeholder="Name"/>
          <!-- Status -->
          <div class="form-control dropdown">
            <span>Status:</span>
            <span>{{editableTask.status}}</span>
            <span id="statusDropDown"
                  class="dropdown-toggle"
                  data-toggle="dropdown"
                  aria-haspopup="true"
                  aria-expanded="false"></span>
            <div class="dropdown-menu dropdown-menu-left" aria-labelledby="statusDropDown">
              <label class="dropdown-item" v-for="status in this.statuses" :key="status">
                <input type="radio"
                       v-model="this.editableTask.status"
                       v-bind:value="status"/>&nbsp;{{status}}
              </label>
            </div>
          </div>
          <!-- Contexts -->
          <div class="form-control dropdown">
            <span>Contexts:</span>
            <span class="mark"
                  v-for="contextId in this.editableTask.contexts" :key="contextId">{{getContextName(contextId)}}</span>
            <span id="contextsDropDown"
                  class="dropdown-toggle float-right"
                  data-toggle="dropdown"
                  aria-haspopup="true"
                  aria-expanded="false"
                  v-if="this.editableTask.status === 'Active'">Change</span>
            <div class="dropdown-menu dropdown-menu-right"
                 aria-labelledby="contextsDropDown"
                 v-if="this.editableTask.status === 'Active'">
              <label class="dropdown-item" v-for="context in this.contexts" :key="context.id">
                <input type="checkbox"
                       name="options"
                       v-model="this.editableTask.contexts"
                       v-bind:value="context.id"/>&nbsp;{{context.name}}
              </label>
            </div>
          </div>
          <!-- Save button -->
          <button v-on:click="this.saveTask(this.editableTask)" class="btn btn-primary form-group" type="button">Save</button>
        </div>
      </div>
    </div>
    <HelloWorld msg="Welcome to Your Vue.js App"/>
  </div>
</template>

<script>
import HelloWorld from './components/HelloWorld.vue'

export default {
  name: 'app',
  components: {
    HelloWorld
  }
}
</script>

<style>
  #app {
    font-family: 'Avenir', Helvetica, Arial, sans-serif;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    color: #2c3e50;
    margin-top: 60px;
  }

  [class*="col-"] {
    background-color: #eee;
    border-right: 2px solid #fff;
    border-bottom: 2px solid #fff;
  }

  .form {
    padding-top: 8px;
    padding-bottom: 8px;
  }

  .form-group {
    margin-top: 2px;
  }

  .form-control {
    margin-top: 2px;
  }
</style>
