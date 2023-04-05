const express = require('express'); //Line 1

//VARIABLES DEFINITION
const app = express(); //Line 2
const port = process.env.PORT || 5000; //Line 3
const bodyParser = require('body-parser');
const {Tabs} = require("@mantine/core");
const {IconNews} = require("@tabler/icons-react");
const React = require("react");



app.use(bodyParser.urlencoded({ extended: true }));
app.use(bodyParser.json());

// This displays message that the server running and listening to specified port
app.listen(port, () => console.log(`Listening on port ${port}`)); //Line 6

// create a GET route
app.get('/express_backend', (req, res) => { //Line 9
    res.send({ express: 'YOUR EXPRESS BACKEND IS CONNECTED TO REACT' }); //Line 10
}); //Line 11

app.post('/add-tab', (req, res) => {

    try {

        const { totalTabs, topic, sectionMsg } = req.body;

        const newTabs = [...totalTabs];

        newTabs.push({

            label: topic,

            content: sectionMsg,

        });

        res.send(newTabs);

    } catch (err) {
        console.error(err);
        res.status(500).send({ error: 'Something went wrong' });
    }
});

app.post('/delete-tab', (req, res) => {

    try {
        const { totalTabs, currentTab } = req.body;

        if (totalTabs.length > 1) {

            const newTabs = [...totalTabs];

            newTabs.splice(currentTab, 1);

            console.log(newTabs);

            res.send(newTabs);

        } else {
            throw new Error('Cannot delete last tab');
        }
    } catch (err) {
        console.error(err);
        res.status(500).send({ error: 'Something went wrong' });
    }
});