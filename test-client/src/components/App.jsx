import React, {
  useCallback, useEffect, useState
} from 'react';
import PropTypes from 'prop-types';
import styled from 'styled-components';
import { weekNumber } from 'weeknumber';
import Tabs from './Tabs';
import List from './List';
import CategoryForm from './CategoryForm';
import EventForm from './EventForm';
import AppActions from './AppActions';

const Container = styled.div`
  display: flex;
  flex-direction: column;
  margin: 20px 250px;
  height: 600px;
  justify-content: space-evenly;
`;

const Row = styled.div`
  display: inline-flex;
  justify-content: space-between;
`;

const DataList = styled(List)`
  height: 150px;
  width: 500px;
`;

const BottomList = styled(DataList)`
  width: 100%;
`;

const ListRow = styled.div`
  display: grid;
  grid-template-columns: ${({ columns }) => columns};
  background-color: ${({ isSelected }) => { const color = isSelected ? '#bbf' : '#fff'; return color; }};
  cursor: pointer;
`;

const ListCell = styled.span`
  max-width: 100%;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
`;

const EventRow = styled.div`
display: flex;
flex-wrap: wrap;
`;

const EventCell = styled(ListCell)`
display: flex;
flex-grow: 20;
justify-content: center;
`;

const BorderedEventCell = styled(EventCell)`
border-right: solid 2px lightgray;
`;

const Venue = ({ name, city, state, }) => (
  <ListRow columns="50% 50%">
    <ListCell>{name}</ListCell>
    <ListCell>{`${city}, ${state}`}</ListCell>
  </ListRow>
);
Venue.propTypes = {
  name: PropTypes.string.isRequired,
  city: PropTypes.string.isRequired,
  state: PropTypes.string.isRequired,
};

const Category = ({
  group, name, desc, isSelected
}) => (
  <ListRow columns="20% 20% 60%" classNames="category" isSelected={isSelected}>
    <ListCell>{group}</ListCell>
    <ListCell>{name}</ListCell>
    <ListCell>{desc}</ListCell>
  </ListRow>
);
Category.propTypes = {
  group: PropTypes.string.isRequired,
  name: PropTypes.string.isRequired,
  desc: PropTypes.string.isRequired,
  isSelected: PropTypes.bool.isRequired,
};

const Events = ({
  event, category, venue, date, buyers,
}) => (
  <EventRow>
    <BorderedEventCell>{event}</BorderedEventCell>
    <BorderedEventCell>{category}</BorderedEventCell>
    <BorderedEventCell>{venue}</BorderedEventCell>
    <BorderedEventCell>{date}</BorderedEventCell>
    <EventCell>{buyers}</EventCell>
  </EventRow>
);
Events.propTypes = {
  event: PropTypes.string.isRequired,
  category: PropTypes.string.isRequired,
  venue: PropTypes.string.isRequired,
  date: PropTypes.string.isRequired,
  buyers: PropTypes.string.isRequired,
};

const App = ({ config: { backendHost } }) => {
  const actions = new AppActions(backendHost);

  const [venues, setVenues] = useState([]);
  const [initialFormContent, setInitialFormContent] = useState({});
  const [knownGroups, setKnownGroups] = useState([]);
  const [categories, setCategories] = useState([]);

  const [categorySelected, setCategorySelected] = useState(undefined);

  const [categoryList, setCategoryList] = useState([]);
  const [venueList, setVenueList] = useState([]);

  const [selectedTab, setSelectedTab] = useState('Event');

  const insertEvent = async ({
    name, datetime, holiday, category, venue
  }) => {
    const days = ['SU', 'MO', 'TU', 'WE', 'TH', 'FR', 'SA'];
    const months = ['JAN', 'FEB', 'MAR', 'APR', 'MAY', 'JUN', 'JUL', 'AUG', 'SEP', 'OCT', 'NOV', 'DEC'];
    const date = new Date(datetime);
    const qtr = Math.floor((date.getMonth() + 2) / 3);
    const year = date.getFullYear();
    const caldate = date.toISOString().match(/(\d{4}-\d{2}-\d{2}).*/)[1];
    const day = days[date.getDay()];
    const week = weekNumber(date);
    const month = months[date.getMonth()];
    const { dateid } = await actions.createDate({
      caldate, day, week, month, qtr, year, holiday
    });
    return actions.createEvent({
      venueid: venue, catid: category, dateid, eventname: name, starttime: date.toISOString()
    });
  };

  const fetchVenues = useCallback(
    () => fetch(`http://${backendHost}/venues`)
      .then(response => response.json())
      .then(setVenueList),
    [backendHost]
  );

  const fetchCategories = useCallback(
    () => fetch(`http://${backendHost}/categories`)
      .then(response => response.json())
      .then(setCategoryList),
    [backendHost]
  );

  const listVenues = useCallback(
    () => fetch(`http://${backendHost}/venues`)
      .then(response => response.json())
      .then(([...list]) => list.map(({
        venueid, venuename, venuecity, venuestate,
      }) => (
        <Venue key={`venue-${venueid}`} city={venuecity} name={venuename} state={venuestate} />
      )))
      .then(resultVenueList => setVenues(resultVenueList)),
    [backendHost]
  );

  const listCategories = useCallback(
    () => fetch(`http://${backendHost}/categories`)
      .then(response => response.json())
      .then(([...list]) => {
        setKnownGroups([...(new Set(list.map(({ catgroup }) => catgroup)))]);
        return list;
      })
      .then(([...list]) => list.map(({
        catid, catgroup, catname, catdesc,
      }) => (
        <Category
          key={`cat-${catid}`}
          desc={catdesc}
          group={catgroup}
          name={catname}
          isSelected={categorySelected === catid}
          onClick={() => {
            setSelectedTab('Category');
            setInitialFormContent({
              catid, catgroup, catname, catdesc,
            });
            setCategorySelected(catid);
          }}
        />
      )))
      .then(resultCategoryList => setCategories(resultCategoryList)),
    [backendHost, categorySelected]
  );

  const [eventList, setEventList] = useState([]);
  const listEvents = useCallback(
    () => fetch(`http://${backendHost}/events`)
      .then(response => response.json())
      .then(([...list]) => list.map(({
        eventid, event, category, venue, date, buyers,
      }) => (
        <Events
          key={`event-${eventid}`}
          event={event}
          category={category}
          venue={venue}
          date={date}
          buyers={buyers}
        />
      )))
      .then(resultEventList => setEventList(resultEventList)),
    [backendHost]
  );

  const upsertCategory = category => actions.upsertCategory(category)
    .then(listCategories);

  const tabItems = [
    {
      label: 'Event',
      content: (<EventForm submit={insertEvent} categories={categoryList} venues={venueList} />)
    }, {
      label: 'Category',
      content: (
        <CategoryForm
          initialContent={initialFormContent}
          knownGroups={knownGroups}
          submit={upsertCategory}
          setCategorySelected={setCategorySelected}
        />
      )
    }
  ];

  useEffect(
    () => {
      listVenues();
      listCategories();
      fetchVenues();
      fetchCategories();
      listEvents();
    },
    [fetchCategories, fetchVenues, listCategories, listVenues, listEvents]
  );

  return (
    <Container>
      <Row>
        <Tabs tabs={tabItems} selectedTab={selectedTab} setSelectedTab={setSelectedTab} />
      </Row>
      <Row>
        <DataList data={categories} />
        <DataList data={venues} />
      </Row>
      <Row>
        <BottomList data={eventList} />
      </Row>
    </Container>
  );
};

App.propTypes = {
  config: PropTypes.object.isRequired
};

export default App;
