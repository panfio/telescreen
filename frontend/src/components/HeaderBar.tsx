import React from 'react';
import { connect } from 'react-redux';
import { apiService } from '../services/ApiService';
import { IRootState } from '../reducers/root-reducer';
import { fetchAll } from '../reducers/actions';
import { setIntervalStart, setIntervalEnd } from '../reducers/time-interval-reducer';

import { DateTimePicker } from '@material-ui/pickers';
import { createStyles, makeStyles, Theme } from '@material-ui/core/styles';
import AppBar from '@material-ui/core/AppBar';
import Toolbar from '@material-ui/core/Toolbar';
import Typography from '@material-ui/core/Typography';
import IconButton from '@material-ui/core/IconButton';
import { Settings, Replay } from '@material-ui/icons';
import MenuItem from '@material-ui/core/MenuItem';
import Menu from '@material-ui/core/Menu';

const useStyles = makeStyles((theme: Theme) =>
  createStyles({
    root: {
      flexGrow: 1,
    },
    menuButton: {
      marginRight: theme.spacing(2),
    },
    title: {
      flexGrow: 1,
    },
  }),
);

export interface IHeaderBarProps extends StateProps, DispatchProps { }


export const HeaderBar = (props: IHeaderBarProps) => {
  const classes = useStyles();
  const [anchorEl, setAnchorEl] = React.useState<null | HTMLElement>(null);
  const open = Boolean(anchorEl);

  const handleMenu = (event: React.MouseEvent<HTMLElement>) => {
    setAnchorEl(event.currentTarget);
  };

  const handleClose = () => {
    setAnchorEl(null);
  };

  return (
    <div className={classes.root}>
      <AppBar position="static">
        <Toolbar>
          <Typography variant="h6" className={classes.title}>
            Telescreen App
          </Typography>
          <DateTimePicker
            autoOk
            variant='inline'
            ampm={false}
            disableFuture
            value={props.timenterval.from}
            onChange={(date) => props.setIntervalStart(date)}
            label="From"
            inputVariant="outlined"
            format="yyyy-MM-dd hh:mm"
            color='primary'
          />
          <DateTimePicker
            variant="inline"
            autoOk
            ampm={false}
            disableFuture
            value={props.timenterval.to}
            onChange={(date) => props.setIntervalEnd(date)}
            label="To"
            showTodayButton
            inputVariant="outlined"
            format="yyyy-MM-dd hh:mm"
          />
          <IconButton
              aria-label="Get items"
              aria-controls="fetch-items"
              onClick={() => props.fetchAll(props.timenterval.from, props.timenterval.to)}
              color="inherit"
            >
              <Replay />
            </IconButton>
          <div>
            <IconButton
              aria-label="menu"
              aria-controls="menu-appbar"
              aria-haspopup="true"
              onClick={handleMenu}
              color="inherit"
            >
              <Settings />
            </IconButton>
            <Menu
              id="menu-appbar"
              anchorEl={anchorEl}
              anchorOrigin={{
                vertical: 'top',
                horizontal: 'right',
              }}
              keepMounted
              transformOrigin={{
                vertical: 'top',
                horizontal: 'right',
              }}
              open={open}
              onClose={handleClose}
            >
              <MenuItem onClick={() => apiService.processAll()}>Process All</MenuItem>
              <MenuItem onClick={() => props.fetchAll(props.timenterval.from, props.timenterval.to)}>Fetch</MenuItem>
            </Menu>
          </div>
        </Toolbar>
      </AppBar>
    </div>
  );
}


const mapStateToProps = ({ timenterval }: IRootState) => ({ timenterval });

const mapDispatchToProps: any = { fetchAll, setIntervalStart, setIntervalEnd };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(HeaderBar);