const express = require('express'); //Line 1

//VARIABLES DEFINITION
const app = express(); //Line 2
const port = process.env.PORT || 5000; //Line 3
const bodyParser = require('body-parser');
const React = require("react");

//FOR USE BODYPARSER
app.use(bodyParser.urlencoded({ extended: true }));
app.use(bodyParser.json());

// This displays message that the server running and listening to specified port
app.listen(port, () => console.log(`Listening on port ${port}`)); //Line 6

//Add tab, method
app.post('/add-tab', (req, res) => {

    try {

        //Get values, from Body
        const { totalTabs, topic, sectionMsg } = req.body;

        //New tabs for be used
        const newTabs = [...totalTabs];

        //Add new TAB
        newTabs.push({

            label: topic,

            content: sectionMsg,

        });

        //Send the new TABS
        res.send(newTabs);

        //Some error, catch it.
    } catch (err) {
        console.error(err);
        res.status(500).send({ error: 'Something went wrong' });
    }
});

//Delete TAB, method
app.post('/delete-tab', (req, res) => {

    try {

        //Get values, from Body
        const { totalTabs, currentTab } = req.body;

        //Have more than 1 TAB, then execute this CODE
        if (totalTabs.length > 1) {

            //Array with tabs, for manipulate the DATA
            const newTabs = [...totalTabs];

            //Delete this
            newTabs.splice(currentTab, 1);

            console.log(newTabs);

            //Send new TABS
            res.send(newTabs);

        } else {
            throw new Error('Cannot delete last tab');
        }
        //Some error, catch .
    } catch (err) {
        console.error(err);
        res.status(500).send({ error: 'Something went wrong' });
    }
});