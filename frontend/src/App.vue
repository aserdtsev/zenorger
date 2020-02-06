<template>
  <div id="app" class="container">
<!--    <img alt="Vue logo" src="./assets/logo.png">-->
    <div class="row text-center">
      <div id="header" class="col-sm-12">Zenorger</div>
    </div>
    <div class="row">
      <div class="col-sm-2">
        <button class="btn btn-link"
                v-bind:class="{ 'btn-outline-primary': selectedListCode === 'Inbox' }"
                v-on:click="showTasks('Inbox')">Inbox
        </button>
        <div>Contexts</div>
        <ul>
          <li v-for="context in contexts" :key="context.id">
            <button class="btn btn-link"
                    v-bind:class="{ 'btn-outline-primary': context.id === selectedListCode }"
                    v-on:click="showTasks(context.id)">{{context.name}}
            </button>
          </li>
        </ul>
        <button class="btn btn-link"
                v-bind:class="{ 'btn-outline-primary': selectedListCode === 'Pending' }"
                v-on:click="showTasks('Pending')">Pending
        </button>
        <button class="btn btn-link"
                v-bind:class="{ 'btn-outline-primary': selectedListCode === 'SomedayMaybe' }"
                v-on:click="showTasks('SomedayMaybe')">Someday / Maybe
        </button>
        <button class="btn btn-link"
                v-bind:class="{ 'btn-outline-primary': selectedListCode === 'Removed' }"
                v-on:click="showTasks('Removed')">Trash
        </button>
      </div>
      <div id="taskList" class="col-sm-5">
        <div id="addTask" class="row">
          <div class="col-sm-12">
            <div class="form">
              <div>
                <label>
                  <input type="text" class="form-control" v-model="newTaskName" placeholder="Add task..."/>
                </label>
              </div>
              <button class="btn btn-primary form-group"
                      v-bind:disabled="!newTaskName"
                      v-on:click="addTask(newTaskName)">Add task
              </button>
            </div>
          </div>
        </div>
        <div class="block">
          <table class="table table-sm table-hover">
            <tbody>
              <tr v-for="task in tasks" :key="task.id">
                <td v-bind:class="{ 'table-success': task.id === editableTask.id }"
                    v-on:click="showTask(task)">
                  <span>{{task.name}}</span>
                  <span class="float-right">{{task.completeDate}}</span>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
      <div id="taskForm" class="col-sm-5">
        <div class="form" v-if="isTaskEdit">
          <!-- Name -->
          <label>
            <input v-model.lazy="editableTask.name" type="text" class="form-control" placeholder="Name"/>
          </label>
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
              <label class="dropdown-item" v-for="status in statuses" :key="status">
                <input type="radio"
                       v-model="editableTask.status"
                       v-bind:value="status"/>&nbsp;{{status}}
              </label>
            </div>
          </div>
          <!-- Contexts -->
          <div class="form-control dropdown">
            <span>Contexts:</span>
            <span class="mark"
                  v-for="contextId in editableTask.contexts" :key="contextId">{{getContextName(contextId)}}</span>
            <span id="contextsDropDown"
                  class="dropdown-toggle float-right"
                  data-toggle="dropdown"
                  aria-haspopup="true"
                  aria-expanded="false">Change</span>
            <div class="dropdown-menu dropdown-menu-right"
                 aria-labelledby="contextsDropDown">
              <label class="dropdown-item" v-for="context in contexts" :key="context.id">
                <input type="checkbox"
                       name="options"
                       v-model="editableTask.contexts"
                       v-bind:value="context.id"/>&nbsp;{{context.name}}
              </label>
            </div>
          </div>
          <!-- Save button -->
          <button v-on:click="saveTask(editableTask)" class="btn btn-primary form-group" type="button">Save</button>
        </div>
      </div>
    </div>
    <HelloWorld msg="Welcome to Your Vue.js App"/>
  </div>
</template>

<script>
    import 'jquery'
    import 'popper.js'
    import 'bootstrap'
    import HelloWorld from './components/HelloWorld.vue'
    import {AXIOS} from './http-common'

    export default {
        name: 'app',
        components: {
            HelloWorld
        },
        data() {
            return {
                statuses: ['Inbox', 'Active', 'Pending', 'SomedayMaybe'],
                contexts: null,
                selectedListCode: 'Inbox',
                tasks: null,
                newTaskName: '',
                editableTask: {},
                editableTaskLastContexts: [],
                isTaskEdit: false
            };
        },
        computed: {
            editableTaskStatus: function () {
                return this.editableTask.status;
            },
            editableTaskContexts: function () {
                return this.editableTask.contexts;
            }
        },
        watch: {
            editableTaskStatus: function (newStatus, oldStatus) {
                if (oldStatus !== undefined && newStatus !== oldStatus && newStatus !== 'Active' &&
                    this.editableTask.contexts !== undefined && this.editableTask.contexts.length > 0)
                    this.editableTask.contexts = [];
            },
            editableTaskContexts: function (newContexts, oldContexts) {
                if (newContexts !== undefined && newContexts.length > 0 && oldContexts !== undefined &&
                    JSON.stringify(newContexts) !== JSON.stringify(oldContexts))
                    this.editableTask.status = 'Active';
            }
        },
        methods: {
            showTasks: function (code) {
                this.selectedListCode = code;
                AXIOS.get('/task/list', {
                    params: {
                        code: code
                    }
                })
                    .then(response => this.tasks = response.data);
                this.isTaskEdit = false;
                this.clearEditableTask();
            },
            addTask: function (taskName) {
                let task = {id: createUuid(), createdAt: new Date(), name: taskName};
                if (this.selectedListCode !== 'Inbox') {
                    task.contexts = [this.selectedListCode];
                    task.status = 'Active';
                }
                AXIOS.post('/task/add', task)
                    .then(response => this.pushTask(response.data));
                this.newTaskName = ''
            },
            showTask: function (task) {
                this.editableTask = jsonCopy(task);
                this.isTaskEdit = true
            },
            saveTask: function (task) {
                AXIOS.post('/task/update', task)
                    .then(response => {
                        this.updateTask(response.data);
                        this.showTasks(this.selectedListCode);
                    });
                this.isTaskEdit = false;
                this.clearEditableTask();
            },
            pushTask: function (task) {
                this.tasks.push(task);
                this.editableTask = task;
            },
            updateTask: function (task) {
                this.editableTask = task;
                let idx = this.tasks.findIndex(it => it.id === task.id);
                this.tasks[idx] = task;
            },
            clearEditableTask: function () {
                this.editableTask = {};
                this.editableTaskLastContexts = [];
            },
            getContextName: function (contextId) {
                return this.contexts.find(item => item.id === contextId).name;
            }
        },
        mounted() {
            AXIOS.get('/context/list')
                .then(response => (this.contexts = response.data));
            this.showTasks('Inbox');
        }
    }

    export function createUuid() {
        return ([1e7] + -1e3 + -4e3 + -8e3 + -1e11).replace(/[018]/g, c =>
            (c ^ crypto.getRandomValues(new Uint8Array(1))[0] & 15 >> c / 4).toString(16)
        )
    }

    function jsonCopy(src) {
        return JSON.parse(JSON.stringify(src));
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
