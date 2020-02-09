import React, { Component } from 'react';
import { Modal, Form, Input, Alert } from 'antd';
import Axios from 'axios';
import { connect } from 'react-redux';

class AddRadioStationModal extends Component {

    state = {
        loading: false,
        successMessage: null,
        errorMessage: null
    };

    handleSubmit = e => {
        e.preventDefault();
        this.setState({ loading: true, successMessage: null, errorMessage: null });
        this.props.form.validateFields((err, values) => {
            if (!err) {
                const config = {
                    headers: {
                        Authorization: `Bearer ${this.props.token}`
                    }
                }

                const content = {
                    ...values
                }

                Axios.post('/admin/radio-stations', content, config)
                    .then(() => this.setState({ ...this.state, loading: false, successMessage: 'Radio station was added' }))
                    .catch(() => this.setState({ ...this.state, loading: false, errorMessage: 'Failed to add radio station' }));
            }
        });
    };

    render() {
        const { getFieldDecorator } = this.props.form;

        const form = (
            <Form labelCol={{ span: 5 }} wrapperCol={{ span: 12 }} onSubmit={this.handleSubmit}>
                <Form.Item label="Title">
                    {getFieldDecorator('title', {
                        rules: [{ required: true, message: 'Please input radio station title!' }],
                    })(<Input />)}
                </Form.Item>
            </Form>
        );

        const successMessage = this.state.successMessage
            ? (<Alert message={this.state.successMessage} showIcon type="success" />)
            : '';

        const errorMessage = this.state.errorMessage
            ? (<Alert message={this.state.errorMessage} showIcon type="error" />)
            : '';
        return (
            <span>
                <Modal
                    title="Add New Radio Station"
                    visible={this.props.visible}
                    okText="Add"
                    okButtonProps={{ disabled: this.state.loading }}
                    onOk={this.handleSubmit}
                    onCancel={this.props.onModalClose}
                >
                    <div style={{ marginBottom: 10 }}>
                        {successMessage}
                        {errorMessage}
                    </div>
                    {form}
                </Modal>
            </span>
        );
    }
}

const form = Form.create({ name: 'coordinated' })(AddRadioStationModal)

const mapStateToProps = (state) => {
    return {
        token: state.auth.token
    }
}

export default connect(mapStateToProps)(form);