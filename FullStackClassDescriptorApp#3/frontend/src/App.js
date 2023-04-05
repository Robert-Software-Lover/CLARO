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

    const buttonStyle = {
        margin: '10px' // Set margin to 10 pixels on all sides
    };

    useEffect(() => {

        if(limit>=10) setLimit(10)

        document.title=`DescriptorApp - Nota # : ${limit}`;

    });

    const addTab = () => {

        if(limit < 10){
            const newTabs = [...tabs];
            const currentDate = new Date(); // Create a new Date object to get the current date
            const formattedDate = currentDate.toLocaleString();
            newTabs.push({
                label: formattedDate,
                content:
                    <Tabs.Panel value={formattedDate} pt="xs">
                        <div className="center-div-container">
                            <h3>Definicion de clase:</h3><br />
                            <textarea id="textarea" name="textarea" rows="30" cols="100" onChange={InputTextChanged}>{definition}</textarea><br /><br /><br />

                            <div className="container">
                                <div className="column">
                                    <h3>Info:</h3><br />
                                </div>
                                <br />
                            </div>

                            <textarea id="textarea" name="textarea" rows="30" cols="150" value={result}></textarea><br /><br /><br />
                        </div>
                    </Tabs.Panel>
            });
            setTabs(newTabs);
            setActiveTab(newTabs.length - 1);
            setDefinition(''); // clear the definition textarea when adding a new tab
        }
        else{
            alert("Limite de tabs: 10");
            setLimit(0)
        }


        setLimit(limit+1)
    };

    const GenerateInfo = (e) => {

        e.preventDefault();

        const content = { body: definition};
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
            <button className="buttonGeneral buttonAdd" style={buttonStyle} onClick={addTab}>+</button>
            <button onClick={GenerateInfo} className="buttonGeneral" >Generar Info</button>

            <Tabs defaultValue="new">
                <Tabs.List>
                    {tabs.map((tab, index) => (
                        <p key={index} className={index === activeTab ? 'active' : ''}>
                            <Tabs.Tab onClick={() => setActiveTab(index)} value={tab.label} icon={<IconNews size="0.8rem" />}>{tab.label}</Tabs.Tab>
                        </p>
                    ))}

                    <div>{tabs[activeTab].content}</div>

                </Tabs.List>


            </Tabs>



        </div>
    );
}

export default App;
