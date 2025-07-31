import {useState, useEffect} from "react";
import axios from 'axios';
import Table from 'react-bootstrap/Table'
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
                        <th>Temperature</th>
                        <th>Humidity</th>
                        <th>Pressure</th>
                    </tr>
                </thead>
                <tbody>
                {conditions.map(c => (
                    <tr key={c.id}>
                        <td>{c.timestamp}</td>
                        <td>{c.tempF}</td>
                        <td>{c.humidity}</td>
                        <td>{c.pressure}</td>
                    </tr>
                ))}
                </tbody>
            </Table>
        </>);
}

export default App
