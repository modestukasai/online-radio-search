import React, { Component } from 'react';
import { createBrowserHistory } from "history";
import { Link } from "react-router-dom";
import { Layout, Menu, Icon } from 'antd';
import { HOME, RADIO_STATIONS, SONGS } from './pathTypes';

class LeftSideMenuComponent extends Component {
    render() {
        const history = createBrowserHistory();
        return (
            <Layout.Sider trigger={null} collapsible collapsed={this.props.collapsed}>
                <div className="logo" />
                <Menu theme="dark" mode="inline" defaultSelectedKeys={[history.location.pathname]}>
                    <Menu.Item key={HOME}>
                        <Icon type="home" /><span>Main</span>
                        <Link to={HOME} />
                    </Menu.Item>
                    <Menu.Item key={RADIO_STATIONS}>
                        <Icon type="unordered-list" /><span>Radio Stations</span>
                        <Link to={RADIO_STATIONS} />
                    </Menu.Item>
                    <Menu.Item key={SONGS}>
                        <Icon type="play-circle" /><span>Songs</span>
                        <Link to={SONGS} />
                    </Menu.Item>
                </Menu>
            </Layout.Sider>
        )
    }
}

export default LeftSideMenuComponent;