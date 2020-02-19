<template>
    <div>
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
</template>

<script>
    export default {
        name: "navigate-bar",
        props: {
            contexts: Array,
            selectedListCode: String
        },
        methods: {
            showTasks: function (code) {
                this.$emit("list-selected", code);
            },
        },
        mounted() {
            this.showTasks('Inbox');
        }
    };
</script>
