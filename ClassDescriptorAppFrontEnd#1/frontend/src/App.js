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

  //Style for speate buttons
  const buttonStyle = {
    margin: '10px' // Set margin to 10 pixels on all sides
  };


  useEffect(() => {

      //Limite equals to 10, useState to 10 all time.
      if(limit>=10) setLimit(10)

      //Change title dinamically
      document.title=`DescriptorApp - Nota # : ${limit}`;

  });

  //Open Dialog Form
  const openDialog = () => {
    setIsOpen(true);
  };

  //Close Dialog Form
  const closeDialog = () => {
    setIsOpen(false);
    setTitle("");
    setContent("")
  };

  //Change Title
  const ChangeTitle = (e) => {

    setTitle(e.target.value);

  };

  //Change Content
  const ChangeContent = (e) => {

    setContent(e.target.value);

  };

  //When Popup BUTTON is clicked, go HERE and call addTab < == real method for add new tab
  const RegisterNote = (e) => {

    e.preventDefault();

    addTab();

    closeDialog();
  };

//Add Tab
  const addTab = () => {

      //Limit < 9, continue adding notes
      if (limit < 10) {

          //Headers for axios
        const headers = {
          'Content-Type': 'application/json',
        };

        //Body for be used, in node side
        const data = {
          totalTabs: tabs,
          topic: title,
          sectionMsg: content
        };

        //Call server side with axios, and pass URL, body and headers
        axios.post('/add-tab', data, {headers})
            .then(response => {

                //Throw this msg in console, for see the status of request and response
              console.log('Codigo: ' + response.status + ', Content: ' + JSON.stringify(response.data))

                //New Tabs Updated
              setTabs(response.data);

              //Current Tab
              setActiveTab(response.data.length - 1);

            })
            //Some Error
            .catch(error => console.error(error));


      }
      //Cant add more tabs, because LIMIT = 10
      else {

        alert("Limite de tabs: 10");

        setLimit(0);
      }

      //Here increment limit + 1
      setLimit(limit + 1);
  };

  const DeleteTab = (e) => {

    e.preventDefault();

      //Headers for axios
    const headers = {
      'Content-Type': 'application/json',
    };

      //Body for be used, in node side
    const data = {
      totalTabs: tabs,
      currentTab: activeTab
    };

    //Call server side with axios, and pass URL, body and headers
    axios.post('/delete-tab', data, {headers})
        .then(response => {

            //Throw this msg in console, for see the status of request and response
            console.log('Codigo: ' + response.status + ', Content: ' + JSON.stringify(response.data))

            //New Tabs Updated
            setTabs(response.data);

            //Current Tab
            setActiveTab(response.data.length - 1);
        })
        //Some Error, Catch it
        .catch(error => console.error(error));


      //Delete 1 from LIMIT
      if(limit > 0) {
          setLimit(limit - 1);
      }
      else {
          // < 0, make it eq to 0
          if(limit < 0) setLimit(0);
      }



  };


  return (
      <div className="App">

          {/* Button for add new TAB */}
        <button className="buttonGeneral buttonAdd" style={buttonStyle} onClick={openDialog}>+</button>

          {/* Button for delete current TAB */}
        <button onClick={DeleteTab} className="buttonGeneral" >Eliminar</button>

          {/* Show and manage TABS */}
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

          {/* When add button is clicked, show this popup */}
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
