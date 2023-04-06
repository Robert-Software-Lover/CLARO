import './App.css';
import React, { Component, useState, useEffect } from 'react';
import { Tabs } from '@mantine/core';
import { IconNews } from '@tabler/icons-react';
import './App.css';
import axios from 'axios';

const App = () => {

    //Variables Definition
    const [tabs, setTabs] = useState([{ label: 'Nuevo', content: '' }]);
    const [activeTab, setActiveTab] = useState(0);
    const [definition, setDefinition] = useState('');
    const [result, setResult] = useState('');
    const [limit, setLimit] = useState(0);
    const [isOpen, setIsOpen] = useState(false);

    const buttonStyle = {
        margin: '10px' // Set margin to 10 pixels on all sides
    };


    useEffect(() => {

        //Limit = 10, keep value in 10
        if(limit>=10) setLimit(10)

        document.title=`DescriptorApp - Nota # : ${limit}`;

    });

    //Open Dialog Form
    const openDialog = (e) => {

        setIsOpen(true);

        //Show result of Info
        GenerateInfo(e);
    };

    //Close Dialog Form
    const closeDialog = () => {
        setIsOpen(false);

    };

    const addTab = () => {

        setDefinition('')

        //Close second textArea
        closeDialog();

        //If limit > 0, keep adding tabs
        if(limit < 10){
            //new array for manipulate data
            const newTabs = [...tabs];
            const currentDate = new Date(); // Create a new Date object to get the current date
            const formattedDate = currentDate.toLocaleString();
            //Add new tab
            newTabs.push({
                //Current date
                label: formattedDate,
                //Content = textArea with definition
                content:
                    <Tabs.Panel value={formattedDate} pt="xs">
                        <div className="center-div-container">
                            <h3>Definicion de clase:</h3><br />
                            <textarea id="textarea" name="textarea" rows="30" cols="100" onChange={InputTextChanged}>{definition}</textarea><br /><br /><br />
                        </div>
                    </Tabs.Panel>

            });
            //Refresh tabs status
            setTabs(newTabs);
            setActiveTab(newTabs.length - 1);
        }
        //Throw this alert, if limit = 10
        else{
            alert("Limite de tabs: 10");
            setLimit(0)
        }

        //Keep incrementing this value
        setLimit(limit+1)
    };

    const GenerateInfo = (e) => {

        e.preventDefault();

        //Body, for be use in node side
        const content = { body: definition};

        //Make the request and get the data for update result
        axios.post('http://localhost:8080/api/descriptor/info', content)
            .then(response =>  setResult(response.data))
            .catch(error => {
                alert('There was an error: ' + error);
            });

        alert('Informe generado.')

    };

    const InputTextChanged = (e) => {

        setDefinition(e.target.value)
    };

    return (
        <div className="App">

            {/* Add new tab, with this button */}
            <button className="buttonGeneral buttonAdd" style={buttonStyle} onClick={addTab}>+</button>

            {/* Generate INFO, with this button */}
            <button onClick={openDialog} className="buttonGeneral" >Generar Info</button>

            {/* Manage and show status from tabs */}
            <Tabs defaultValue="new">
                <Tabs.List>
                    {tabs.map((tab, index) => (
                        <p key={index} className={index === activeTab ? 'active' : ''}>
                            <Tabs.Tab onClick={() => setActiveTab(index)} value={tab.label} icon={<IconNews size="0.8rem" />}>{tab.label}</Tabs.Tab>
                        </p>
                    ))}

                    <div>{tabs[activeTab].content}</div>

                </Tabs.List>

                {/* Show this textArea, when button INFO is clicked */}
                {isOpen && (

                    <div className="center-div-container-result">

                        <div className="container">
                            <div className="column">
                                <h3>Info:</h3><br />
                            </div>
                            <br />
                        </div>
                        <textarea id="textarea" name="textarea" rows="30" cols="150" value={result}></textarea><br /><br /><br />
                    </div>
                )}


            </Tabs>


        </div>
    );
}

export default App;
