<template>
    <div>
        <v-data-table
            :headers="headers"
            :items="cargoes"
            :options.sync="options"
            :server-items-length="totalCargoes"
            :loading="loading"
            :footer-props="{
              showFirstLastPage: true,
              firstIcon: 'mdi-arrow-collapse-left',
              lastIcon: 'mdi-arrow-collapse-right',
              prevIcon: 'mdi-minus',
              nextIcon: 'mdi-plus'
            }"
            multi-sort
            class="elevation-1"
        ></v-data-table>
    </div>
</template>

<script>
    export default {
        name: "JobsTable",

        data () {
            return {
                totalCargoes: 0,
                cargoes: [],
                loading: true,
                options: {},
                headers: [
                    { text: 'Id', value: 'id' },
                    { text: 'Source Country', value: 'source.city.countryOrState.name' },
                    { text: 'Source City', value: 'source.city.name' },
                    { text: 'Target Country', value: 'target.city.countryOrState.name' },
                    { text: 'Target City', value: 'target.city.name' },
                    { text: 'Cargo', value: 'cargo.internalId' },
                    { text: 'Distance (km)', value: 'shortestDistanceKm' },
                    { text: 'Source DLC', value: 'source.city.dlc' },
                    { text: 'Target DLC', value: 'target.city.dlc' },
                    { text: 'Heavy Power Cargo', value: 'highPowerCargo', sortable: false },
                    { text: 'Heavy Cargo', value: 'heavyCargo', sortable: false }
                ]
            }
        },

        watch: {
            options: {
                handler () {
                    this.getDataFromApi()
                        .then(data => {
                            this.loading = false;
                            this.cargoes = data.content;
                            this.totalCargoes = data.totalElements;
                        })
                },
                deep: true,
            }
        },

        mounted () {
            this.getDataFromApi()
                .then(data => {
                    this.loading = false;
                    this.cargoes = data.content;
                    this.totalCargoes = data.totalElements;
                })
        },

        methods: {
            getDataFromApi () {
                this.loading = true;

                const { sortBy, sortDesc, page, itemsPerPage } = this.options;

                let url = `jobs?page=${page - 1}&size=${itemsPerPage}`;
                if (sortBy.length > 0) {
                    let dirJoin = sortDesc.map(dir => dir ? 'DESC' : 'ASC').join(',');
                    let sortJoin = sortBy.join(',');
                    url += `&sortDirection=${dirJoin}&sortBy=${sortJoin}`
                }
                //console.log(url);
                return fetch(url)
                    .then((resp) => resp.json());
            }
        }
    }
</script>

<style scoped>

</style>