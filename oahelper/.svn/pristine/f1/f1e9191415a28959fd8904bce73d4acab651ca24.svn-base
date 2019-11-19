<template>
    <div class="p48 bg-white">
        <Row>
            <Col span="24">
                <Form ref="roomInfo"
                      :model="roomInfo"
                      :rules="roomRules"
                      :label-width="100"
                      @keydown.native.enter.prevent="submit('roomInfo')">
                    <FormItem prop="dd">
                        <label class="grey-light-color"
                               slot="label">
                            {{$t('menu.Location')}}
                        </label>
                        <Row>
                            <Col span="6">
                                <FormItem prop="selectedCountry">
                                    <Select v-model="roomInfo.selectedCountry"
                                            @on-change="changeCountry"
                                            :placeholder="$t('placeholder.country')"
                                            clearable>
                                        <Option v-for="item in countrys"
                                                :value="item.countryName"
                                                :key="item.countryName">
                                            {{item.countryName}}
                                        </Option>
                                    </Select>
                                </FormItem>
                            </Col>
                            <Col span="5" offset="1">
                                <FormItem prop="selectedCity">
                                    <Select v-model="roomInfo.selectedCity"
                                            @on-change="changeCity"
                                            :placeholder="$t('placeholder.city')"
                                            clearable>
                                        <Option v-for="item in citys"
                                                :value="item.cityName"
                                                :key="item.cityName">
                                            {{item.cityName}}
                                        </Option>
                                    </Select>
                                </FormItem>
                            </Col>
                            <Col span="5" offset="1">
                                <FormItem prop="buildingId">
                                    <Select v-model="roomInfo.buildingId"
                                            @on-change="changeBuilding"
                                            :placeholder="$t('placeholder.building_s')"
                                            clearable>
                                        <Option v-for="item in buildings"
                                                :value="item.id"
                                                :key="item.id">
                                            {{item.buildingName}}
                                        </Option>
                                    </Select>
                                </FormItem>
                            </Col>
                            <Col span="5" offset="1">
                                <FormItem prop="floor">
                                    <Select v-model="roomInfo.floor"
                                            :placeholder="$t('placeholder.floor')"
                                            clearable>
                                        <Option v-for="item in floors"
                                                :value="item"
                                                :key="item">
                                            {{item}}
                                        </Option>
                                    </Select>
                                </FormItem>
                            </Col>
                        </Row>
                    </FormItem>
                    <FormItem prop="roomName">
                        <label class="grey-light-color"
                               slot="label">
                            {{$t('message.name')}}
                        </label>
                        <Row>
                            <Col span="8">
                                <Input v-model="roomInfo.roomName"
                                       :placeholder="$t('placeholder.name')"
                                       type="text"
                                       clearable></Input>
                            </Col>
                            <Col span="15" class="por">
                                <Icon type="md-information-circle"
                                      color="#99CC66"
                                      class="infoicon"/>
                                <div class="infotext">
                                    {{$t('title.roomTip1')}}
                                </div>
                            </Col>
                        </Row>
                    </FormItem>
                    <FormItem prop="roomTele">
                        <label class="grey-light-color"
                               slot="label">
                            {{$t('message.telephone')}}
                        </label>
                        <Row>
                            <Col span="8">
                                <Input v-model="roomInfo.roomTele"
                                       :placeholder="$t('placeholder.telephone')"
                                       type="text"
                                       clearable></Input>
                            </Col>
                        </Row>
                    </FormItem>
                    <FormItem prop="roomEmail">
                        <label class="grey-light-color"
                               slot="label">
                            {{$t('message.email')}}
                        </label>
                        <Input v-model="roomInfo.roomEmail"
                               :placeholder="$t('placeholder.email')"
                               type="text"
                               clearable></Input>
                    </FormItem>
                    <FormItem prop="roomType">
                        <label class="grey-light-color"
                               slot="label">
                            {{$t('message.type')}}
                        </label>
                        <Row>
                            <Col span="8">
                                <Select v-model="roomInfo.roomType"
                                        :placeholder="$t('placeholder.type')"
                                        @on-change="changeRoomType"
                                        clearable>
                                    <Option v-for="item in dicts.ROOMTYPE"
                                            :value="item.id"
                                            :key="item.id">
                                        {{item.text}}
                                    </Option>
                                </Select>
                            </Col>
                            <Col span="15"
                                 class="por">
                                <Icon v-if="roomInfo.roomType == '2' || roomInfo.roomType == '3' || roomInfo.roomType == '4' ||roomInfo.roomType == '5'"
                                      type="md-information-circle"
                                      color="#99CC66"
                                      class="infoicon"/>
                                <div class="infotext"
                                     v-if="roomInfo.roomType == '2'">
                                    {{$t('title.roomTip2')}}
                                </div>
                                <div class="infotext"
                                     v-if="roomInfo.roomType == '3'">
                                    {{$t('title.roomTip3')}}
                                </div>
                                <div class="infotext"
                                     v-if="roomInfo.roomType == '4'">
                                    {{$t('title.roomTip4')}}
                                </div>
                                <div class="infotext"
                                     v-if="roomInfo.roomType == '5'">
                                    {{$t('title.roomTip5')}}
                                </div>
                            </Col>
                        </Row>
                    </FormItem>
                    <FormItem prop="ownerId"
                              v-if="roomInfo.roomType == '3'||roomInfo.roomType == '4'||roomInfo.roomType == '5'">
                        <label class="grey-light-color"
                               slot="label">
                            {{$t('message.owner')}}
                        </label>
                        <Row>
                            <Col span="8">
                                <Select filterable v-model="roomInfo.ownerId"
                                        :placeholder="$t('placeholder.owner')"
                                        clearable>
                                    <Option v-for="item in dicts.SysUsers"
                                            :value="item.id"
                                            :key="item.id">{{item.text}}
                                    </Option>
                                </Select>
                            </Col>
                        </Row>
                    </FormItem>
                    <FormItem prop="adminId">
                        <label class="grey-light-color"
                               slot="label">
                            {{$t("message.admin")}}
                        </label>
                        <Row>
                            <Col span="8">
                                <Select v-model="roomInfo.adminId"
                                        :placeholder="$t('placeholder.admin')"
                                        clearable filterable>
                                    <Option v-for="item in dicts.SysUsers"
                                            :value="item.id" :key="item.id">{{item.text}}
                                    </Option>
                                </Select>
                            </Col>
                            <Col span="15"
                                 class="por">
                                <Checkbox v-model="roomInfo.sendFlagText"
                                          class="admincheck">
                                    {{$t('title.sendFlagInfo')}}
                                </Checkbox>
                            </Col>
                        </Row>
                    </FormItem>
                    <FormItem prop="roomSize">
                        <label class="grey-light-color"
                               slot="label">
                            {{$t('message.size')}}
                        </label>
                        <Row>
                            <Col span="8">
                                <Select v-model="roomInfo.roomSize"
                                        :placeholder="$t('placeholder.size')"
                                        clearable>
                                    <Option v-for="item in dicts.ROOMSIZE"
                                            :value="item.id"
                                            :key="item.id">
                                        {{item.text}}
                                    </Option>
                                </Select>
                            </Col>
                            <Col span="15"
                                 class="por">
                                <Icon type="md-information-circle"
                                      color="#99CC66"
                                      class="infoicon"/>
                                <div class="infotext" style="padding-right: 10px">
                                    {{$t('title.roomTip6')}}
                                </div>
                            </Col>
                        </Row>
                    </FormItem>
                    <FormItem prop="layout">
                        <label class="grey-light-color"
                               slot="label">
                            {{$t('message.layout')}}
                            <Input style="display:none"
                                   v-model="roomInfo.layout"
                                   type="text"></Input>
                        </label>
                        <div>
                            <Row class="text-center">
                                <Col span="8">
                                    {{$t("message.photo")}} 1
                                </Col>
                                <Col span="8">
                                    {{$t("message.photo")}} 2
                                </Col>
                                <Col span="8">
                                    {{$t("message.photo")}} 3
                                </Col>
                            </Row>
                            <Row>
                                <Col span="8">
                                    <div class="demo-upload-list">
                                        <template v-if="imga!=''">
                                            <div :style="{backgroundImage:'url('+(imga)+')',backgroundSize:'100%'}" class=loyoutImg></div>
                                            <div class="demo-upload-list-cover">
                                                <Icon type="ios-eye-outline"
                                                      @click.native="handleView(imga)"></Icon>
                                                <Icon type="ios-trash-outline"
                                                      @click.native="deleteImga"></Icon>
                                            </div>
                                        </template>
                                        <template>
                                            <Upload v-if="imga==''"
                                                    type="drag"
                                                    :action="apiUrl+'/uploadfile/toLocal'"
                                                    :show-upload-list="false"
                                                    :format="['jpg','jpeg','png']"
                                                    :on-format-error="handleFormatError"
                                                    :on-success="uploadImgSucess0"
                                                    style="flex:1">
                                                <Icon type="ios-cloud-upload"
                                                      size="24"
                                                      style="color: #3399ff"></Icon>
                                                {{$t("message.upload")}}
                                            </Upload>
                                            <Progress v-if="showProgress"
                                                      :percent="percentage"
                                                      hide-info></Progress>
                                        </template>
                                    </div>
                                </Col>
                                <Col span="8">
                                    <div class="demo-upload-list">
                                        <template v-if="imgb!=''">
                                            <div :style="{backgroundImage:'url('+(imgb)+')',backgroundSize:'100%'}" class=loyoutImg></div>
                                            <div class="demo-upload-list-cover">
                                                <Icon type="ios-eye-outline"
                                                      @click.native="handleView(imgb)"></Icon>
                                                <Icon type="ios-trash-outline"
                                                      @click.native="deleteImgb"></Icon>
                                            </div>
                                        </template>
                                        <template>
                                            <Upload v-if="imgb==''"
                                                    type="drag"
                                                    :action="apiUrl+'/uploadfile/toLocal'"
                                                    :show-upload-list="false"
                                                    :format="['jpg','jpeg','png']"
                                                    :on-format-error="handleFormatError"
                                                    :on-success="uploadImgSucess1"
                                                    style="flex:1">
                                                <Icon type="ios-cloud-upload"
                                                      size="24"
                                                      style="color: #3399ff"></Icon>
                                                {{$t("message.upload")}}
                                            </Upload>
                                            <Progress v-if="showProgress"
                                                      :percent="percentage"
                                                      hide-info></Progress>
                                        </template>
                                    </div>
                                </Col>
                                <Col span="8">
                                    <div class="demo-upload-list">
                                        <template v-if="imgc!=''">
                                            <div :style="{backgroundImage:'url('+(imgc)+')',backgroundSize:'100%'}" class=loyoutImg></div>
                                            <div class="demo-upload-list-cover">
                                                <Icon type="ios-eye-outline"
                                                      @click.native="handleView(imgc)"></Icon>
                                                <Icon type="ios-trash-outline"
                                                      @click.native="deleteImgc"></Icon>
                                            </div>
                                        </template>
                                        <template>
                                            <Upload v-if="imgc==''"
                                                    type="drag"
                                                    :action="apiUrl+'/uploadfile/toLocal'"
                                                    :show-upload-list="false"
                                                    :format="['jpg','jpeg','png']"
                                                    :on-format-error="handleFormatError"
                                                    :on-success="uploadImgSucess2"
                                                    style="flex:1">
                                                <Icon type="ios-cloud-upload"
                                                      size="24"
                                                      style="color: #3399ff"></Icon>
                                                {{$t("message.upload")}}
                                            </Upload>
                                            <Progress v-if="showProgress"
                                                      :percent="percentage"
                                                      hide-info></Progress>
                                        </template>
                                    </div>
                                </Col>
                            </Row>
                        </div>
                    </FormItem>
                    <FormItem prop="zoomFlag">
                        <label class="grey-light-color"
                               slot="label">
                            {{$t("message.hasZoom")}}
                        </label>
                        <RadioGroup v-model="roomInfo.zoomFlag">
                            <Radio v-for="item in dicts.ZOOMFLAG"
                                   :label="item.id">
                                {{item.text}}
                            </Radio>
                        </RadioGroup>
                    </FormItem>
                    <FormItem prop="zoomKey" v-if="roomInfo.zoomFlag==1">
                        <label class="grey-light-color"
                               slot="label">
                            {{$t('message.zoomKey')}}
                        </label>
                        <Row>
                            <Col span="8">
                                <Input v-model="roomInfo.zoomKey"
                                       :placeholder="$t('placeholder.zoomKey')"
                                       type="text"
                                       clearable></Input>
                            </Col>
                        </Row>
                    </FormItem>
                    <FormItem prop="zoomSecret" v-if="roomInfo.zoomFlag==1">
                        <label class="grey-light-color"
                               slot="label">
                            {{$t('message.zoomSecret')}}
                        </label>
                        <Row>
                            <Col span="8">
                                <Input v-model="roomInfo.zoomSecret"
                                       :placeholder="$t('placeholder.zoomSecret')"
                                       type="text"
                                       clearable></Input>
                            </Col>
                        </Row>
                    </FormItem>
                    <FormItem prop="zoomUserid" v-if="roomInfo.zoomFlag==1">
                        <label class="grey-light-color"
                               slot="label">
                            {{$t('message.zoomUserId')}}
                        </label>
                        <Row>
                            <Col span="8">
                                <Input v-model="roomInfo.zoomUserid"
                                       :placeholder="$t('placeholder.zoomUserId')"
                                       type="text"
                                       clearable></Input>
                            </Col>
                        </Row>
                    </FormItem>

                    <FormItem prop="tabletIp">
                        <label class="grey-light-color"
                               slot="label">
                            {{$t('message.tabletIp')}}
                        </label>
                        <Row>
                            <Col span="8">
                                <Input v-model="roomInfo.tabletIp"
                                       :placeholder="$t('placeholder.tabletIp')"
                                       type="text"
                                       clearable></Input>
                            </Col>
                        </Row>
                    </FormItem>
                    <FormItem prop="roomFunctionArr">
                        <label class="grey-light-color"
                               slot="label">
                            {{$t('message.function')}}
                        </label>
                        <CheckboxGroup v-model="roomInfo.roomFunctionArr">
                            <Checkbox v-for="item in dicts.ROOMFUNCTION"
                                      :label="item.id">
                                {{item.text}}
                            </Checkbox>
                        </CheckboxGroup>
                    </FormItem>
                    <FormItem prop="equipment">
                        <label class="grey-light-color"
                               slot="label">
                            {{$t('message.equipment')}}
                        </label>
                        <Row class="text-right">
                            <Button type="error"
                                    size="small"
                                    @click="delEquip">
                                {{$t('message.delete')}}
                            </Button>
                            &nbsp;
                            <Button type="primary"
                                    size="small"
                                    @click="openEquipModal">
                                {{$t('message.add')}}
                            </Button>
                        </Row>
                        <Table :columns="columnsEquipment"
                               :data="roomEquipmentList"
                               @on-selection-change='selectionTable'>
                        </Table>
                    </FormItem>
                    <FormItem prop="roomStatus">
                        <label class="grey-light-color"
                               slot="label">
                            {{$t('message.status')}}
                        </label>
                        <Row>
                            <Col span="22">
                                <RadioGroup v-model="roomInfo.roomStatus">
                                    <Radio v-for="item in dicts.ROOMSTATUS"
                                           :label="item.id"
                                           :key="item.id">
                                        {{item.text}}
                                    </Radio>
                                </RadioGroup>
                            </Col>
                        </Row>
                    </FormItem>
                </Form>
            </Col>
        </Row>
        <div class="text-right">
            <Button :disabled="loading"
                    @click="cancel">
                {{$t("message.cancel")}}
            </Button>
            &nbsp;&nbsp;
            <Button type="primary"
                    :loading="loading"
                    @click="submit('roomInfo')">
                {{$t("message.submit")}}
            </Button>
        </div>
        <Modal v-model="equipModal"
               width="800">
            <div slot="header">
                <h3>
                    {{$t('message.equipment')}}
                </h3>
            </div>
            <div>
                <Row>
                    <Col span="6">
                        <Input class="mr10"
                               clearable
                               v-model="searchModel.keyword"
                               placeholder="keyword"
                               clearable></Input>
                    </Col>
                    <Col span="6" offset="1">
                        <Select v-model="searchModel.equipType"
                                :placeholder="$t('placeholder.type_nd')"
                                clearable>
                            <Option v-for="item in equipmentType"
                                    :value="item.id"
                                    :key="item.id">
                                {{item.text}}
                            </Option>
                        </Select>
                    </Col>
                    <Col span="6" offset="1">
                        <Select v-model="searchModel.equipStatus"
                                :placeholder="$t('placeholder.status')"
                                clearable>
                            <Option v-for="item in equipmentStatus"
                                    :value="item.id"
                                    :key="item.id">
                                {{item.text}}
                            </Option>
                        </Select>
                    </Col>
                    <Col span="3" offset="1">
                        <Button @click="openEquipModal"
                                icon="ios-search">
                            {{$t('message.search')}}
                        </Button>
                    </Col>
                </Row>
            </div>
            <br>
            <Table ref="selection"
                   :loading="loadingEquip"
                   :columns="columnsEquipment"
                   :data="equipItems"
                   @on-selection-change='selectionModal'>
            </Table>
            <br>
            <Page :total="total"
                  :page-size="searchModel.pageSize"
                  :current="searchModel.pageNum"
                  show-elevator
                  show-total
                  @on-change="changePage"
                  class="fr"/>
            <div class="clear"></div>
            <div slot="footer">
                <Button @click="closeEquipModal">
                    {{$t("message.cancel")}}
                </Button>
                <Button type="primary"
                        @click="addEquip">
                    {{$t("message.confirm")}}
                </Button>
            </div>
        </Modal>
        <Modal v-model="visible"
               draggable>
            <div slot="header">
                <h3>{{$t("message.picture")}}</h3>
            </div>
            <img :src="imgUrl"
                 style="width: 100%">
            <div slot="footer">
                <Button @click="visible=false">
                    {{$t("message.close")}}
                </Button>
            </div>
        </Modal>
    </div>
</template>

<script src="../../components/sc/roomlist_edit.js"></script>

<style type="text/css" scoped>
    .infoicon {
        font-size: 18px;
        width: 20px;
        position: absolute;
        top: 10px;
        left: 10px;
        cursor: pointer;
        z-index: 9;
    }

    .infotext {
        width: calc(100% - 20px);
        display: inline-block;
        position: absolute;
        left: 30px;
        top: 12px;
        line-height: 16px;
    }

    .admincheck {
        width: calc(100% - 10px);
        margin-left: 10px;
    }

    .demo-upload-list {
        display: inline-block;
        width: 100%;
        height: 100%;
        max-width: 300px;
        max-height: 225px;
        text-align: center;
        line-height: 60px;
        margin-right: 5px;
    }

    .demo-upload-list img {
        width: 100%;
        height: 100%;
    }

    .demo-upload-list-cover {
        display: none;
        position: absolute;
        top: 0;
        bottom: 0;
        left: 0;
        right: 0;
        background: rgba(0, 0, 0, .6);
    }

    .demo-upload-list:hover .demo-upload-list-cover {
        display: block;
    }

    .demo-upload-list-cover i {
        color: #fff;
        font-size: 40px;
        cursor: pointer;
        margin: 0 2px;
    }
    .loyoutImg {
        height: 150px;
    }
</style>