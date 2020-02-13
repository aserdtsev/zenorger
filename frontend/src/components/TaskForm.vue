<template>
  <div class="form">
    <!-- Name -->
    <label>
      <input v-model.lazy="task.name" type="text" class="form-control" placeholder="Name"/>
    </label>
    <!-- Status -->
    <div class="form-control dropdown">
      <span>Status:</span>
      <span>{{task.status}}</span>
      <span id="statusDropDown"
            class="dropdown-toggle"
            data-toggle="dropdown"
            aria-haspopup="true"
            aria-expanded="false"></span>
      <div class="dropdown-menu dropdown-menu-left" aria-labelledby="statusDropDown">
        <label class="dropdown-item" v-for="status in statuses" :key="status">
          <input type="radio"
                 v-model="task.status"
                 v-bind:value="status"/>&nbsp;{{status}}
        </label>
      </div>
    </div>
    <!-- Contexts -->
    <div class="form-control dropdown">
      <span>Contexts:</span>
      <span class="mark"
            v-for="contextId in task.contexts" :key="contextId">{{getContextName(contextId)}}</span>
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
                 v-model="task.contexts"
                 v-bind:value="context.id"/>&nbsp;{{context.name}}
        </label>
      </div>
    </div>
    <!-- Save button -->
    <button v-on:click="saveTask(task)" class="btn btn-primary form-group" type="button">Save</button>
  </div>
</template>

<script>

import {jsonCopy} from "@/main";

export default {
    name: 'task-form',
    props: {
        initialTask: Object,
        statuses: Array,
        contexts: Array
    },
    data() {
        return {
            task: jsonCopy(this.initialTask)
        };
    },
    computed: {
        taskStatus: function () {
            return this.task.status;
        },
        taskContexts: function () {
            return this.task.contexts;
        }
    },
    watch: {
        taskStatus: function (newStatus, oldStatus) {
            if (oldStatus !== undefined && newStatus !== oldStatus && newStatus !== 'Active' &&
                this.task.contexts !== undefined && this.task.contexts.length > 0)
                this.task.contexts = [];
        },
        taskContexts: function (newContexts, oldContexts) {
            if (newContexts !== undefined && newContexts.length > 0 && oldContexts !== undefined &&
                JSON.stringify(newContexts) !== JSON.stringify(oldContexts))
                this.task.status = 'Active';
        }
    },
    methods: {
        getContextName: function (contextId) {
            return this.contexts.find(item => item.id === contextId).name;
        },
        saveTask: function (task) {
            this.$emit('task-edit-completed', task);
        }
    }
}

</script>

<style scoped>
  @import '../assets/app.css';
</style>
