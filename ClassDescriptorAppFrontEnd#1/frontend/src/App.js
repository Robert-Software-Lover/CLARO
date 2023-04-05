import './App.css';
import React, { Component, useState, useEffect } from 'react';
import {Alert, Tabs} from '@mantine/core';
import { IconNews } from '@tabler/icons-react';
import './App.css';
import axios from 'axios';

const App = () => {

  //Variables Definition
  const [tabs, setTabs] = useState([{ label: 'Nuevo', content: '' }]);
  const [activeTab, setActiveTab] = useState(0);
  const [limit, setLimit] = useState(0);
  const [isOpen, setIsOpen] = useState(false);
  const [title, setTitle] = useState("");
  const [content, setContent] = useState("");

  const buttonStyle = {
    margin: '10px' // Set margin to 10 pixels on all sides
  };


  useEffect(() => {

    if(limit>=10) setLimit(10)

    document.title=`DescriptorApp - Nota # : ${limit}`;

  });

  const openDialog = () => {
    setIsOpen(true);
  };

  const closeDialog = () => {
    setIsOpen(false);
    setTitle("");
    setContent("")
  };

  const ChangeTitle = (e) => {

    setTitle(e.target.value);
  };

  const ChangeContent = (e) => {

    setContent(e.target.value);
  };

  const RegisterNote = (e) => {
    // Perform desired action with inputValue
    e.preventDefault();

    addTab();

    closeDialog();
  };


  const addTab = () => {


      if (limit < 10) {

        const headers = {
          'Content-Type': 'application/json',
        };

        const data = {
          totalTabs: tabs,
          topic: title,
          sectionMsg: content
        };

        axios.post('/add-tab', data, {headers})
            .then(response => {
              console.log('Codigo: ' + response.status + ', Content: ' + JSON.stringify(response.data))
              setTabs(response.data);
              setActiveTab(response.data.length - 1);
            })
            .catch(error => console.error(error));


      }
      else {

        alert("Limite de tabs: 10");

        setLimit(0);
      }

      setLimit(limit + 1);
  };

  const DeleteTab = (e) => {

    e.preventDefault();

    const headers = {
      'Content-Type': 'application/json',
    };

    const data = {
      totalTabs: tabs,
      currentTab: activeTab
    };

    axios.post('/delete-tab', data, {headers})
        .then(response => {
          console.log('Codigo: ' + response.status + ', Content: ' + JSON.stringify(response.data))
          setTabs(response.data);
          setActiveTab(response.data.length - 1);
        })
        .catch(error => console.error(error));

  };


  return (
      <div className="App">
        <button className="buttonGeneral buttonAdd" style={buttonStyle} onClick={openDialog}>+</button>
        <button onClick={DeleteTab} className="buttonGeneral" >Eliminar</button>

        <Tabs defaultValue="new">
          <Tabs.List>
            {tabs.map((tab, index) => (
                <p key={index} className={index === activeTab ? 'active' : ''}>
                  <Tabs.Tab onClick={() => setActiveTab(index)} value={tab.label} icon={<IconNews size="0.8rem" />}>{tab.label}</Tabs.Tab>
                </p>
            ))}
          </Tabs.List>

          {tabs.map((tab, index) => (
              <Tabs.Panel key={index} value={tab.label} pt="xs">
                <div className="center-div-container">
                  <h3>{tab.content}</h3>
                </div>
              </Tabs.Panel>
          ))}
        </Tabs>

        {isOpen && (
        <div className="center-div-container-form">
          <form onSubmit={RegisterNote}>

            <input type="text" onChange={ChangeTitle} placeholder="Titulo"/>

            <input type="text" onChange={ChangeContent} placeholder="Contenido"/>

              <input type="submit" value="Agregar"/>

            </form>
        </div>
        )}

      </div>
  );
}

export default App;
