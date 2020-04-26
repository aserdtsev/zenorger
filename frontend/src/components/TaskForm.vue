<template>
  <div class="form">
    <fieldset>
      <!--suppress HtmlFormInputWithoutLabel -->
      <input class="form-control" v-model="task.name" type="text" placeholder="Name"/>
    </fieldset>
    <fieldset id="status" class="form-control dropdown">
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
    </fieldset>
    <fieldset id="contexts" class="form-control dropdown">
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
    </fieldset>
    <fieldset id="description">
      <label>Description</label>
      <button v-bind:disabled="descriptionMode === 'edit'"
              v-on:click="setDescriptionMode('edit')"
              class="btn btn-link btn-sm">Edit
      </button>
      <div v-if="descriptionMode === 'view'"
           class="white-space-pre"
           v-linkified>{{task.description}}
      </div>
      <div v-show="descriptionMode === 'edit'">
        <textarea v-model="task.description"
                  class="form-control"/>
        <div class="btn-group" role="group">
          <button v-if="descriptionMode === 'edit'"
                  v-on:click="saveDescription()"
                  class="btn btn-primary btn-sm"
                  type="button">Save</button>
          <button v-on:click="cancelDescription()"
                  class="btn btn-link btn-sm"
                  type="button">Cancel</button>
        </div>
      </div>
    </fieldset>
    <button id="save"
            v-on:click="save()"
            v-bind:disabled="!isModified"
            class="btn btn-primary form-group"
            type="button">Save</button>
    <button id="cancel"
            v-on:click="cancel()"
            v-bind:disabled="!isModified"
            class="btn btn-link form-group"
            type="button">Cancel</button>
  </div>
</template>

<script>

import {jsonCopy, equals} from "@/main";

export default {
    name: 'task-form',
    props: {
        initialTask: Object,
        statuses: Array,
        contexts: Array
    },
    data() {
        return {
            task: jsonCopy(this.initialTask),
            descriptionMode: "view",
            oldDescription: null
        };
    },
    computed: {
        taskStatus: function () {
            return this.task.status;
        },
        taskContexts: function () {
            return this.task.contexts;
        },
        isModified: function () {
            return !equals(this.task, this.initialTask);
        }
    },
    watch: {
        initialTask: function(newValue) {
            this.task = jsonCopy(newValue);
        },
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
        saveDescription: function () {
            this.setDescriptionMode('view');
        },
        cancelDescription: function () {
          this.task.description = this.oldDescription;
          this.setDescriptionMode('view');
        },
        setDescriptionMode: function(mode) {
            if (mode === 'edit')
                this.oldDescription = this.task.description;
            this.descriptionMode = mode;
        },
        save: function () {
            this.$emit('task-edit-completed', this.task);
        },
        cancel: function() {
            this.task = jsonCopy(this.initialTask);
        }
    }
}

</script>

<style scoped>
  @import '../assets/app.css';
</style>
