import React, { useRef, useState } from 'react';
import PropTypes from 'prop-types';
import styled from 'styled-components';
import {
  Col,
  Form,
  Button,
} from 'react-bootstrap';

const StyledForm = styled(Form)`
  * {font-size: 14px;}
`;

const StyledMessage = styled.div`
  color: #1e7e34;
  font-weight: 600;
  margin-bottom: 15px;
`;

const EventForm = ({ venues, categories, submit }) => {
  const [name, setName] = useState('');
  const removeTZandms = isoString => isoString.match(/(\d{4}-\d{2}-\d{2}T\d{2}:\d{2}).*/)[1];
  const now = removeTZandms(new Date().toISOString());

  const [datetime, setDatetime] = useState(now);
  const [category, setCategory] = useState(0);
  const [venue, setVenue] = useState(0);
  const [holiday, setHoliday] = useState(false);
  const [submitMessage, setSubmitMessage] = useState('');
  const timeout = useRef(-1);

  const resetTimeout = () => {
    if (timeout.current) {
      clearTimeout(timeout.current);
    }
  };

  const cleanup = () => {
    setDatetime(now);
    setCategory(0);
    setVenue(0);
    setHoliday(false);
    resetTimeout();
  };

  // useEffect(() => cleanup(), []);
  return (
    <StyledForm onSubmit={(event) => {
      event.preventDefault();
      cleanup();
      submit({
        name, datetime, holiday, category, venue
      });
      setSubmitMessage('Submitted!');
      timeout.current = setTimeout(() => {
        setSubmitMessage('');
      }, 500);
    }}
    >
      <Form.Row>
        <Form.Group as={Col} controlId="venue">
          <Form.Label>Venue</Form.Label>
          <Form.Control as="select" value={venue} onChange={({ target: { value } }) => setVenue(value)} data-testid="event-venue">
            {venues.map(({ venueid, venuename, }) => (
              <option value={venueid} key={`cat-${venueid}`}>{venuename}</option>
            ))}
          </Form.Control>
        </Form.Group>
        <Form.Group as={Col} controlId="category">
          <Form.Label>Category</Form.Label>
          <Form.Control as="select" value={category} onChange={({ target: { value } }) => setCategory(value)} data-testid="event-category">
            {categories.map(({ catid, catname, }) => (
              <option value={catid} key={`cat-${catid}`}>{catname}</option>
            ))}
          </Form.Control>
        </Form.Group>
        <Form.Group as={Col} controlId="name">
          <Form.Label>Name</Form.Label>
          <Form.Control
            value={name}
            onChange={({ target: { value } }) => setName(value)}
            placeholder="Event name"
            data-testid="event-name"
          />
        </Form.Group>
        <Form.Group as={Col} controlId="event-datetime">
          <Form.Label>Date and time</Form.Label>
          <Form.Control
            type="datetime-local"
            value={datetime}
            min={now}
            onChange={({ target: { value } }) => setDatetime(value)}
            data-testid="event-datetime"
          />
        </Form.Group>
        <Form.Group as={Col} controlId="holiday">
          <Form.Label>Is a holiday</Form.Label>
          <Form.Check
            value={holiday}
            onChange={({ target: { checked } }) => setHoliday(checked)}
            data-testid="event-holiday"
          />
        </Form.Group>
      </Form.Row>
      {submitMessage && <StyledMessage>{submitMessage}</StyledMessage>}
      <Button variant="primary" type="submit" data-testid="submit-event-button">
        Submit
      </Button>
    </StyledForm>
  );
};

EventForm.defaultProps = {
  submit: () => null
};

EventForm.propTypes = {
  venues: PropTypes.array.isRequired,
  categories: PropTypes.array.isRequired,
  submit: PropTypes.func,
};

export default EventForm;
