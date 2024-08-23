package edu.stevens.cs522.chat.entities;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.time.Instant;
import java.util.UUID;

/**
 * Created by dduggan.
 */

// TODO annotate (including FK constraints)
// You must also declare indices on the FK columns, otherwise integrity checking
// may trigger a linear search of this table.
@Entity(tableName = "Message",
        foreignKeys = {
                @ForeignKey(entity = Peer.class, parentColumns = "name", childColumns = "sender", onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = Chatroom.class, parentColumns = "name", childColumns = "chatroom", onDelete = ForeignKey.CASCADE)
        },
        indices = {@Index(value = "sender"), @Index(value = "chatroom")}
)
public class Message implements Parcelable {

    // TODO annotate
    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "chatroom")
    public String chatroom;

    @ColumnInfo(name = "messageText")
    public String messageText;

    // Unique id, assigned by server
    public long seqNum;

    // The id of the app that created this message
    @TypeConverters(UUIDConverter.class)
    public UUID appID;

    @ColumnInfo(name = "timestamp")
    public Instant timestamp;

    @ColumnInfo(name = "latitude")
    public Double latitude;

    @ColumnInfo(name = "longitude")
    public Double longitude;

    @ColumnInfo(name = "sender")
    public String sender;

    public Message() {
    }

    public Message(Parcel in) {
        id = in.readLong();
        chatroom = in.readString();
        messageText = in.readString();
        seqNum = in.readLong();
        appID = UUID.fromString(in.readString());
        timestamp = TimestampConverter.deserialize(in.readString());
        latitude = in.readDouble();
        longitude = in.readDouble();
        sender = in.readString();
    }

    @Override
    public String toString() {
        return messageText;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeLong(id);
        out.writeString(chatroom);
        out.writeString(messageText);
        out.writeLong(seqNum);
        out.writeString(appID.toString());
        out.writeString(TimestampConverter.serialize(timestamp));
        out.writeDouble(latitude);
        out.writeDouble(longitude);
        out.writeString(sender);
    }

    public static final Creator<Message> CREATOR = new Creator<Message>() {

        @Override
        public Message createFromParcel(Parcel source) {
            return new Message(source);
        }

        @Override
        public Message[] newArray(int size) {
            return new Message[size];
        }

    };

}