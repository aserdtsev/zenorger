<template>
  <div class="form">
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
</template>

<script>
import {AXIOS} from "@/http-common";

export default {
    name: 'TaskEdit',
    props: {
        editableTask: Object,
        tasks: Array
    },
    data() {
        return {
            editableTaskLastContexts: []
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
            const idx = this.tasks.findIndex(it => it.id === task.id);
            this.tasks[idx] = task;
        },
        clearEditableTask: function () {
            this.editableTask = {};
            this.editableTaskLastContexts = [];
        }
    }
}
</script>
