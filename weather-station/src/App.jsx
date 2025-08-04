import {useState, useEffect} from "react";
import axios from 'axios';
import Table from 'react-bootstrap/Table'
import { NumericFormat } from 'react-number-format';
import './App.css'

function App() {
    const [conditions, setConditions] = useState([]);

    useEffect(() => {
        axios.get("/api/data")
            .then(resp => {
                console.log(resp);
                setConditions(resp.data);
            })
            .catch(error => console.error(error))
    });

    return (
        <>
            <h2>Current Conditions</h2>
            <Table striped bordered size="sm">
                <thead>
                    <tr>
                        <th>Date/Time</th>
                        <th>Temperature (&#176;F)</th>
                        <th>Humidity (%)</th>
                        <th>Pressure (in Hg)</th>
                    </tr>
                </thead>
                <tbody>
                {conditions.map(c => (
                    <tr key={c.id}>
                        <td>{c.timestamp.substring(5,7)}/{c.timestamp.substring(8,10)}/{c.timestamp.substring(0,4)}&nbsp;
                            {c.timestamp.substring(11,13)}:{c.timestamp.substring(14,16)}:{c.timestamp.substring(17,19)}</td>
                        <td><NumericFormat displayType="text" value={c.tempF} decimalScale={1} /> </td>
                        <td><NumericFormat displayType="text" value={c.humidity} decimalScale={1} /> </td>
                        <td><NumericFormat displayType="text" value={c.pressure*2.953e-4} decimalScale={1} /> </td>
                    </tr>
                ))}
                </tbody>
            </Table>
        </>);
}

export default App
